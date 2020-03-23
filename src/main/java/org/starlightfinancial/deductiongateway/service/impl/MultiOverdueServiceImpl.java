package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.goldeye.MultiOverdue;
import org.starlightfinancial.deductiongateway.domain.goldeye.MultiOverdueRepository;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.remote.*;
import org.starlightfinancial.deductiongateway.dto.SurplusTotalAmount;
import org.starlightfinancial.deductiongateway.service.MultiOverdueService;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 多期逾期管理Service接口实现类
 * @date: Created in 2020/2/20 14:57
 * @Modified By:
 */
@Service
public class MultiOverdueServiceImpl implements MultiOverdueService {

    @Autowired
    private MultiOverdueRepository multiOverdueRepository;

    @Autowired
    private RepaymentPlanRepository repaymentPlanRepository;

    @Autowired
    private RepaymentActualRepository repaymentActualRepository;

    @Autowired
    private DataWorkInfoRepository dataWorkInfoRepository;

    /**
     * 计算剩余应还金额
     *
     * @param accountManager 卡号信息
     * @return
     */
    @Override
    public SurplusTotalAmount obtainSurplusTotalAmount(AccountManager accountManager) {

        SurplusTotalAmount surplusTotalAmount = new SurplusTotalAmount();
        surplusTotalAmount.setOverdueFlag(false);
        //判断是否逾期,没有逾期的话不需要特别的处理,直接计算当期应还金额
        List<MultiOverdue> overdueList =
                multiOverdueRepository.findByDateIdAndPlanTermDateLessThanEqualAndOverdueDaysGreaterThanOrderByPlanTermDateAsc(accountManager.getDateId(), new Date(), 0);
        if (overdueList.size() > 0) {
            surplusTotalAmount.setOverdueFlag(true);
            BigDecimal totalPrincipalAndInterest = BigDecimal.ZERO;
            BigDecimal totalServiceFee = BigDecimal.ZERO;

            //已经逾期,需要判断逾期时间是否在2020.1.1之前,overdueList已经是按计划还款日升序排序后的结果
            MultiOverdue firstOverdue = overdueList.get(0);
            //2020.1.1减去首次逾期期数的计划还款日,计算中间的差值
            Date date = Utility.convertToDate("2020-01-01", "yyyy-MM-dd");
            long between = Utility.between(firstOverdue.getPlanTermDate(), date);
            if (between > 0) {
                //是2020.1.1之前逾期的
                //2020.1.1之前逾期的本息及罚息
                BigDecimal principalAndInterestAndPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) > 0 && multiOverdue.getPlanTypeId() == 1212)
                        .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest()).add(multiOverdue.getSerplusRepaymentPenalty())
                                , BigDecimal::add);
                //2020.1.1之前逾期的服务费及罚息
                BigDecimal serviceFeePrincipalAndPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) > 0 && multiOverdue.getPlanTypeId() == 1214)
                        .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest()).add(multiOverdue.getSerplusRepaymentPenalty())
                                , BigDecimal::add);

                totalPrincipalAndInterest = totalPrincipalAndInterest.add(principalAndInterestAndPenalty.setScale(2, RoundingMode.HALF_UP));
                totalServiceFee = totalServiceFee.add(serviceFeePrincipalAndPenalty.setScale(2, RoundingMode.HALF_UP));
            }
            //2020.1.1之后逾期的本息没有罚息,凌晨跑出来的数据多期逾期表中包含了当天应还款的本息
            BigDecimal principalAndInterestWithoutPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1212)
                    .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest())
                            , BigDecimal::add);
            //2020.1.1之前逾期的服务费没有罚息,凌晨跑出来的数据多期逾期表中包含了当天应还款的本息
            BigDecimal serviceFeePrincipalWithoutPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1214)
                    .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest())
                            , BigDecimal::add);


            //查询本息剩余挂账金额
            RepaymentActual repaymentActual = repaymentActualRepository.findFirstByDateIdAndPlanTypeIdOrderByIdDesc(Long.parseLong(accountManager.getDateId().toString()), 1212);
            if (Objects.nonNull(repaymentActual) && repaymentActual.getTotalHangingAmount() > 0) {
                //如果最后一条还款登记记录有大于0的挂账金额,需要减去挂账
                totalPrincipalAndInterest = totalPrincipalAndInterest.subtract(BigDecimal.valueOf(repaymentActual.getTotalHangingAmount()));
            }
            //查询本息剩余挂账金额
            RepaymentActual serviceFeeRepaymentActual = repaymentActualRepository.findFirstByDateIdAndPlanTypeIdOrderByIdDesc(Long.parseLong(accountManager.getDateId().toString()), 1214);
            if (Objects.nonNull(serviceFeeRepaymentActual) && serviceFeeRepaymentActual.getTotalHangingAmount() > 0) {
                //如果最后一条还款登记记录有大于0的挂账金额,需要减去挂账
                totalServiceFee = totalServiceFee.subtract(BigDecimal.valueOf(serviceFeeRepaymentActual.getTotalHangingAmount()));
            }
            //逾期本息(罚息)汇总
            totalPrincipalAndInterest = totalPrincipalAndInterest.add(principalAndInterestWithoutPenalty).setScale(2, RoundingMode.HALF_UP);
            //逾期服务费(罚息)汇总
            totalServiceFee = totalServiceFee.add(serviceFeePrincipalWithoutPenalty).setScale(2, RoundingMode.HALF_UP);
            surplusTotalAmount.setPrincipalAndInterest(totalPrincipalAndInterest);
            surplusTotalAmount.setServiceFee(totalServiceFee);

        }

        return surplusTotalAmount;
    }

    /**
     * 获取豁免信息
     *
     * @param accountManager  卡号信息
     * @param repaymentAmount 已还金额
     * @param applyDate       豁免申请还款时间
     * @param repaymentType   还款类别
     * @return
     */
    @Override
    public SurplusTotalAmount obtainExemptInfo(AccountManager accountManager, BigDecimal repaymentAmount, Date applyDate, Integer repaymentType) {
        SurplusTotalAmount surplusTotalAmount = new SurplusTotalAmount();
        surplusTotalAmount.setOverdueFlag(false);
        surplusTotalAmount.setClearFlag(false);
        surplusTotalAmount.setExemptInfos(new ArrayList<>());
        //判断是否逾期,没有逾期的话不需要特别的处理
        List<MultiOverdue> overdueList =
                multiOverdueRepository.findByDateIdAndPlanTypeIdAndPlanTermDateLessThanEqualAndOverdueDaysGreaterThanOrderByPlanTermDateAsc(accountManager.getDateId(), repaymentType, applyDate, 0);
        DataWorkInfo dataWorkInfo = dataWorkInfoRepository.findByDateId(accountManager.getDateId());
        if (overdueList.size() > 0) {
            surplusTotalAmount.setOverdueFlag(true);
            surplusTotalAmount.setClearFlag(true);
            //循环判断足够结清几期
            Date date = Utility.convertToDate("2020-01-01", "yyyy-MM-dd");
            long between = 0L;
            for (int i = 0; i < overdueList.size(); i++) {
                MultiOverdue multiOverdue = overdueList.get(i);
                //当期应还总金额
                BigDecimal planTotalAmount = multiOverdue.getSerplusRepaymentPrincipal().add(multiOverdue.getSerplusRepaymentInterest());
                //计算2020-1-1  -  计划还款日 差值
                between = Utility.between(multiOverdue.getPlanTermDate(), date);
                //计算罚息
                BigDecimal penalty = calculatePenalty(multiOverdue, applyDate, dataWorkInfo);
                multiOverdue.setSerplusRepaymentPenalty(penalty);

                if (between > 0) {
                    //如果计划还款时间是在2020-1-1之前的,不能豁免罚息,应还金额需要加上罚息
                    planTotalAmount = planTotalAmount.add(penalty);

                } else {
                    //如果是在2020-1-1之后的,如果有罚息的需要豁免罚息,目前规则只运行到3月31日
                    if (penalty.compareTo(BigDecimal.ZERO) > 0 && repaymentAmount.compareTo(BigDecimal.ZERO) > 0) {
                        //剩余还款金额大于0时才保存豁免信息
                        ExemptInfo exemptInfo = createExemptInfo(applyDate, multiOverdue);
                        surplusTotalAmount.getExemptInfos().add(exemptInfo);
                    }
                }

                if (repaymentAmount.compareTo(planTotalAmount) >= 0) {
                    //如果还款金额>=当期应还总金额 ,还款金额-当期应还总金额 后继续循环
                    repaymentAmount = repaymentAmount.subtract(planTotalAmount).setScale(2, RoundingMode.HALF_UP);

                } else {
                    //如果还款金额<当期应还总金额
                    if (i == 0) {
                        //如果i=0说明还款金额不足以结清逾期第一期,需要给出提示或保存记录,如果i>0,说明第i期没有完全结清,不用特殊操作
                        surplusTotalAmount.setClearFlag(false);
                        break;
                    }
                }

            }
        }

        return surplusTotalAmount;
    }

    /**
     * 计算罚息
     *
     * @param multiOverdue  逾期信息
     * @param repaymentDate 还款日期
     * @param dataWorkInfo
     * @return
     */
    private BigDecimal calculatePenalty(MultiOverdue multiOverdue, Date repaymentDate, DataWorkInfo dataWorkInfo) {
        //查询某期的最近一次还款记录
        RepaymentActual repaymentActual = repaymentActualRepository.findFirstByDateIdAndRepaymentTermAndPlanTypeIdOrderByIdDesc(Long.parseLong(multiOverdue.getDateId().toString()), multiOverdue.getPlanTerm(), multiOverdue.getPlanTypeId());
        //计划还款日期
        Date planTermDate = multiOverdue.getPlanTermDate();
        if (Objects.nonNull(repaymentActual)) {
            //如果最近一次还款记录不为空,比较还款日期与计划还款日期,取较大值
            //最后一次还款记录还款日期 -  计划还款日
            long between = Utility.between(planTermDate, repaymentActual.getRepaymentTermDate());
            if (between > 0) {
                //计划还款日期在最后一次还款日期及其以后,使用计划还款日期计算逾期天数,
                //计划还款日期在最后一次还款日期之前,使用最后一次还款日期计算逾期天数
                planTermDate = repaymentActual.getRepaymentTermDate();
            }
        }

        //计算逾期天数
        long overdueDays = Utility.between(planTermDate, repaymentDate);
        //计算罚息:分为本息和服务费两种情况
        BigDecimal planTotalAmount = multiOverdue.getSerplusRepaymentPrincipal().add(multiOverdue.getSerplusRepaymentInterest());
        if (multiOverdue.getPlanTypeId() == 1212) {
            BigDecimal rate = BigDecimal.valueOf(dataWorkInfo.getRate()).setScale(2, RoundingMode.HALF_UP);
            BigDecimal result = planTotalAmount.multiply(BigDecimal.valueOf(1.5));
            result = result.multiply(rate);
            result = result.multiply(BigDecimal.valueOf(overdueDays));
            result = result.divide(BigDecimal.valueOf(3000), RoundingMode.HALF_UP);
            return result;
        } else {
            return planTotalAmount.multiply(BigDecimal.valueOf(0.001)).multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
        }

    }


    /**
     * 生成豁免申请信息
     *
     * @param applyRepaymentDate 申请还款日期
     * @param multiOverdue       逾期信息
     * @return
     */
    private ExemptInfo createExemptInfo(Date applyRepaymentDate, MultiOverdue multiOverdue) {
        int amountType = multiOverdue.getPlanTypeId() == 1212 ? 1224 : 1228;
        ExemptInfo exemptInfo = new ExemptInfo();
        exemptInfo.setDateId(multiOverdue.getDateId());
        exemptInfo.setPlanTerm(multiOverdue.getPlanTerm());
        exemptInfo.setApplyRepaymentDate(applyRepaymentDate);
        exemptInfo.setExemptType(1);
        exemptInfo.setExemptPrincipal(0f);
        exemptInfo.setExemptInterest(0f);
        exemptInfo.setExemptDamage(0f);
        exemptInfo.setPlanPrincipal(multiOverdue.getSerplusRepaymentPrincipal().floatValue());
        exemptInfo.setPlanInterest(multiOverdue.getSerplusRepaymentInterest().floatValue());
        exemptInfo.setExemptPenalty(multiOverdue.getSerplusRepaymentPenalty().floatValue());
        exemptInfo.setPlanPenalty(multiOverdue.getSerplusRepaymentPenalty().floatValue());
        exemptInfo.setPlanDamage(0f);
        exemptInfo.setReason("");
        exemptInfo.setApplyDate(new Date());
        exemptInfo.setSubmitterNo(2214);
        exemptInfo.setSubmitter("黄钟峤");
        exemptInfo.setInitialReviewResult("同意！");
        exemptInfo.setInitialReviewDate(exemptInfo.getApplyDate());
        exemptInfo.setInitialReviewerNo(2214);
        exemptInfo.setInitialReviewer("黄钟峤");
        exemptInfo.setFinalReviewResult("疫情原因延迟还款豁免");
        exemptInfo.setFinalReviewDate(exemptInfo.getApplyDate());
        exemptInfo.setFinalReviewerNo(2214);
        exemptInfo.setFinalReviewer("黄钟峤");
        exemptInfo.setApplyStatus(99);
        exemptInfo.setPlanTypeId(multiOverdue.getPlanTypeId());
        exemptInfo.setExemptItem(1221);
        exemptInfo.setAmountType(amountType);
        exemptInfo.setDepartment("");
        exemptInfo.setAuthorizer("");
        exemptInfo.setOperator("");
        exemptInfo.setRemark("疫情原因延迟还款豁免");
        exemptInfo.setContractNo(null);
        exemptInfo.setCustomerName(null);
        exemptInfo.setUsedStatus(0);
        return exemptInfo;
    }

}
