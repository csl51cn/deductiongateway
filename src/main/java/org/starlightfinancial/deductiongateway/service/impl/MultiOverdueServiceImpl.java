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
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * @param needExemptInfo 是否需要豁免信息
     * @param applyDate      豁免申请还款时间
     * @return
     */
    @Override
    public SurplusTotalAmount obtainSurplusTotalAmount(AccountManager accountManager, Boolean needExemptInfo, Date applyDate) {

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
            //2020.1.1之后逾期的本息没有罚息
            BigDecimal principalAndInterestWithoutPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1212)
                    .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest())
                            , BigDecimal::add);
            //2020.1.1之前逾期的服务费没有罚息
            BigDecimal serviceFeePrincipalWithoutPenalty = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1214)
                    .reduce(BigDecimal.ZERO, (amount, multiOverdue) -> amount.add(multiOverdue.getPlanRepaymentPrincipal()).add(multiOverdue.getPlanRepaymentInterest())
                            , BigDecimal::add);
            //通过计划还款日期查询当日应还,仅限2020.3月份,限制查询的起止日期.3月份之后需要及时清理标记信息.
            Date startDate = Utility.convertToDate(Utility.convertToString(new Date(), "yyyy-MM-dd"), "yyyy-MM-dd");


            //当期应还的本息
            RepaymentPlan principalAndInterestRepaymentPlan = repaymentPlanRepository.findByDateIdAndPlanTermDateAndPlanTypeIdAndStatus(Long.parseLong(accountManager.getDateId().toString()), startDate, 1212, "0");
            BigDecimal currentPrincipalAndInterest = BigDecimal.ZERO;
            if (Objects.nonNull(principalAndInterestRepaymentPlan)) {
                //查询本息剩余挂账金额
                RepaymentActual repaymentActual = repaymentActualRepository.findFirstByDateIdAndPlanTypeIdOrderByIdDesc(Long.parseLong(accountManager.getDateId().toString()), 1212);
                currentPrincipalAndInterest = BigDecimal.valueOf(principalAndInterestRepaymentPlan.getPlanTotalAmount()).setScale(2, RoundingMode.HALF_UP);
                if (Objects.nonNull(repaymentActual) && repaymentActual.getTotalHangingAmount() > 0) {
                    //如果最后一条还款登记记录有大于0的挂账金额,需要减去挂账
                    currentPrincipalAndInterest = currentPrincipalAndInterest.subtract(BigDecimal.valueOf(repaymentActual.getTotalHangingAmount()));
                }
            }

            //当期应还的服务费
            RepaymentPlan serviceFeeRepaymentPlan = repaymentPlanRepository.findByDateIdAndPlanTermDateAndPlanTypeIdAndStatus(Long.parseLong(accountManager.getDateId().toString()), startDate, 1214, "0");
            BigDecimal currentServiceFee = BigDecimal.ZERO;
            if (Objects.nonNull(serviceFeeRepaymentPlan)) {
                //如果当期应还服务费不为空，有些业务可能没有服务费
                //查询本息剩余挂账金额
                RepaymentActual serviceFeeRepaymentActual = repaymentActualRepository.findFirstByDateIdAndPlanTypeIdOrderByIdDesc(Long.parseLong(accountManager.getDateId().toString()), 1214);
                currentServiceFee = BigDecimal.valueOf(serviceFeeRepaymentPlan.getPlanTotalAmount()).setScale(2, RoundingMode.HALF_UP);
                if (Objects.nonNull(serviceFeeRepaymentActual) && serviceFeeRepaymentActual.getTotalHangingAmount() > 0) {
                    //如果最后一条还款登记记录有大于0的挂账金额,需要减去挂账
                    currentServiceFee = currentServiceFee.subtract(BigDecimal.valueOf(serviceFeeRepaymentActual.getTotalHangingAmount()));
                }
            }


            //逾期本息(罚息)汇总
            totalPrincipalAndInterest = totalPrincipalAndInterest.add(principalAndInterestWithoutPenalty).add(currentPrincipalAndInterest).setScale(2, RoundingMode.HALF_UP);
            //逾期服务费(罚息)汇总
            totalServiceFee = totalServiceFee.add(serviceFeePrincipalWithoutPenalty).add(currentServiceFee).setScale(2, RoundingMode.HALF_UP);
            surplusTotalAmount.setPrincipalAndInterest(totalPrincipalAndInterest);
            surplusTotalAmount.setServiceFee(totalServiceFee);


            //豁免信息
            if (needExemptInfo) {
                //格式化豁免申请还款时间为yyyy-MM-dd格式
                Date applyRepaymentDate = Utility.convertToDate(Utility.convertToString(applyDate, "yyyy-MM-dd"), "yyyy-MM-dd");
                DataWorkInfo dataWorkInfo = dataWorkInfoRepository.findByDateId(accountManager.getDateId());
                //本息豁免信息
                List<ExemptInfo> principalAndInterestExemptInfo = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1212)
                        .map(multiOverdue -> createExemptInfo(dataWorkInfo, applyRepaymentDate, multiOverdue, 1212)).collect(Collectors.toList());
                //本息豁免信息
                List<ExemptInfo> serviceFeeExemptInfo = overdueList.stream().filter(multiOverdue -> Utility.between(multiOverdue.getPlanTermDate(), date) <= 0 && multiOverdue.getPlanTypeId() == 1214)
                        .map(multiOverdue -> createExemptInfo(dataWorkInfo, applyRepaymentDate, multiOverdue, 1214)).collect(Collectors.toList());
                surplusTotalAmount.setPrincipalAndInterestExemptInfos(principalAndInterestExemptInfo);
                surplusTotalAmount.setServiceFeeExemptInfos(serviceFeeExemptInfo);
            }
        }

        return surplusTotalAmount;
    }

    /**
     * 生成豁免申请信息
     *
     * @param dataWorkInfo
     * @param applyRepaymentDate 申请还款日期
     * @param multiOverdue       逾期信息
     * @param planTypeId         还款类别
     * @return
     */
    private ExemptInfo createExemptInfo(DataWorkInfo dataWorkInfo, Date applyRepaymentDate, MultiOverdue multiOverdue, Integer planTypeId) {
        int amountType = planTypeId == 1212 ? 1224 : 1228;
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
        //计算罚息
        BigDecimal planPenalty;
        //计算逾期天数
        long overdueDays = Math.abs(Utility.between(applyRepaymentDate, multiOverdue.getPlanTermDate()));
        BigDecimal planTotalAmount = multiOverdue.getPlanTotalAmount();
        if (planTypeId == 1212) {
            //本息罚息计算:未还本息*1.5*月利率/100/30*逾期天数
            planPenalty = planTotalAmount.multiply(BigDecimal.valueOf(1.5)).multiply(BigDecimal.valueOf(dataWorkInfo.getRate()))
                    .divide(BigDecimal.valueOf(3000), 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
        } else {
            //服务费罚息计算:未还本息*0.001*逾期天数
            planPenalty = planTotalAmount.multiply(BigDecimal.valueOf(0.001)).multiply(BigDecimal.valueOf(overdueDays)).setScale(2, RoundingMode.HALF_UP);
        }
        exemptInfo.setExemptPenalty(planPenalty.floatValue());
        exemptInfo.setPlanPenalty(planPenalty.floatValue());
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
        exemptInfo.setPlanTypeId(planTypeId);
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
