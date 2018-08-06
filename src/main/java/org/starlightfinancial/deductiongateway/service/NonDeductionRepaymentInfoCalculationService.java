package org.starlightfinancial.deductiongateway.service;

import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.deductiongateway.domain.local.AssociatePayer;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentPlan;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentPlanRepository;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.RepaymentTypeEnum;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息查找对应业务信息服务类
 * @date: Created in 2018/7/24 9:32
 * @Modified By:
 */

public class NonDeductionRepaymentInfoCalculationService {


    private static AssociatePayerService associatePayerService = SpringContextUtil.getBean(AssociatePayerService.class);
    private static RepaymentPlanRepository repaymentPlanRepository = SpringContextUtil.getBean(RepaymentPlanRepository.class);

    /**
     * 尝试查找非代扣还款信息对应的业务信息
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     */
    public static void searchBusinessTransaction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo) {
        //首先获取所有非代扣还款数据对应的业务信息
        //获得excel导入的客户名
        String customerName = nonDeductionRepaymentInfo.getCustomerName();
        //非代扣还款数据的可能对应业务的合同编号与业务信息的映射
        ConcurrentHashMap<String, BusinessTransaction> candidateBusinessTransactionMap = new ConcurrentHashMap<>(16);
        //获取缓存中的所有业务信息
        Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();

        //通过非代扣还款数据的客户名称对比在业务系统中的借款人名称,个人共借人担保人名称,企业共借人担保人名称,如果有相同的,将那条业务信息添加到tempMap中,以非代扣还款数据为key,业务信息为value
        compareCoBorrowerAndGuarantor(customerName, candidateBusinessTransactionMap, businessTransactionCacheMap);

        //通过非代扣还款数据的客户名称与关联还款人信息中的关联还款人1,2,3,4对比,如果有相同的,将那条业务信息添加到tempMap中,以非代扣还款数据为key,业务信息为value
        List<AssociatePayer> allAssociatePayer = Collections.synchronizedList(associatePayerService.findAll());
        allAssociatePayer.parallelStream().forEach(associatePayer -> {
            if (StringUtils.isNotBlank(customerName)) {
                if (customerName.equals(associatePayer.getPayer1())) {
                    putPossibleBusinessTransaction(candidateBusinessTransactionMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer2())) {
                    putPossibleBusinessTransaction(candidateBusinessTransactionMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer3())) {
                    putPossibleBusinessTransaction(candidateBusinessTransactionMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer4())) {
                    putPossibleBusinessTransaction(candidateBusinessTransactionMap, associatePayer);
                }
            }
        });

        List<BusinessTransaction> candidateBusinessTransactions = Collections.synchronizedList(new ArrayList<>(candidateBusinessTransactionMap.values()));

        //业务结清判断,如果结清了从candidateBusinessTransactions中移除当前业务信息.
        Iterator<BusinessTransaction> iterator = candidateBusinessTransactions.iterator();
        while (iterator.hasNext()) {
            BusinessTransaction next = iterator.next();
            //查询未结清期数
            Long count = repaymentPlanRepository.countByDateIdAndStatus(next.getDateId(), "0");
            if (count == 0) {
                //如果业务已经结清了,移除
                iterator.remove();
            }
        }


        //获得了所有可能对应的业务信息即候选业务信息,判断还款日期和还款金额是否在允许的误差范围内
        TreeSet<BusinessTransaction> resultSet = new TreeSet<>();
        if (candidateBusinessTransactions.size() == 1) {
            //如果候选的业务信息只有一条,直接加入到resultSet中
            resultSet.add(candidateBusinessTransactions.get(0));
        } else {
            //如果候选的业务信息不止一条,继续用还款日期和还款金额去匹配
            compareDateAndAmount(nonDeductionRepaymentInfo, candidateBusinessTransactions, resultSet);

        }



        //最后判断resultSet中有多少条业务信息,如果result的大小不为1,表示未精确查询到匹配的业务信息,不做出更改,如果只剩下一条对应的业务信息,将合同编号,dateId设置到非代扣还款信息中
        if (resultSet.size() == 1) {
            nonDeductionRepaymentInfo.setDateId(resultSet.first().getDateId());
            nonDeductionRepaymentInfo.setContractNo(resultSet.first().getContractNo());
            nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.SUCCESS.getCode());
        }
    }

    /**
     * 比较还款日期和还款金额是否在允许误差范围内,如果是添加到resultSet中
     *
     * @param nonDeductionRepaymentInfo     非代扣还款信息
     * @param candidateBusinessTransactions 业务信息
     * @param resultSet                     可能匹配的业务信息
     */
    private static void compareDateAndAmount(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, List<BusinessTransaction> candidateBusinessTransactions, TreeSet<BusinessTransaction> resultSet) {
        //与计划还款信息中的还款日期和还款金额对比,缩小范围,还款日期±一天,还款金额±1元
        candidateBusinessTransactions.forEach(businessTransaction -> {
            Integer codeByDesc = RepaymentTypeEnum.getCodeByDesc(nonDeductionRepaymentInfo.getRepaymentType());
            if (codeByDesc != null) {
                //获取还款类别代码不为null,查询对应还款类别的未结清的期数最小的还款计划信息
                RepaymentPlan repaymentPlan = repaymentPlanRepository.findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(businessTransaction.getDateId(), codeByDesc, "0");
                if (repaymentPlan != null) {
                    concreteCompareDateAndAmount(nonDeductionRepaymentInfo, resultSet, businessTransaction, repaymentPlan);
                }

            } else {
                //获取还款类别代码为null,查询所有还款类别的计划还款信息
                //本息
                RepaymentPlan repaymentPlan = repaymentPlanRepository.findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(businessTransaction.getDateId(), RepaymentTypeEnum.PRINCIPAL_AND_INTEREST.getCode(), "0");
                if (repaymentPlan != null) {
                    concreteCompareDateAndAmount(nonDeductionRepaymentInfo, resultSet, businessTransaction, repaymentPlan);
                }
                //服务费
                repaymentPlan = repaymentPlanRepository.findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(businessTransaction.getDateId(), RepaymentTypeEnum.SERVICE_FEE.getCode(), "0");
                if (repaymentPlan != null) {
                    concreteCompareDateAndAmount(nonDeductionRepaymentInfo, resultSet, businessTransaction, repaymentPlan);
                }
                //调查评估费
                repaymentPlan = repaymentPlanRepository.findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(businessTransaction.getDateId(), RepaymentTypeEnum.EVALUATION_FEE.getCode(), "0");
                if (repaymentPlan != null) {
                    concreteCompareDateAndAmount(nonDeductionRepaymentInfo, resultSet, businessTransaction, repaymentPlan);
                }
            }

        });
    }

    /**
     * 比较非代扣还款数据中的客户名称和共借人担保人以获取匹配的业务信息
     *
     * @param customerName                    客户名称
     * @param candidateBusinessTransactionMap 可能对应业务的合同编号与业务信息的映射
     * @param businessTransactionCacheMap     所有业务信息
     */
    private static void compareCoBorrowerAndGuarantor(String customerName, Map<String, BusinessTransaction> candidateBusinessTransactionMap, Map<String, BusinessTransaction> businessTransactionCacheMap) {
        businessTransactionCacheMap.forEach((contractNo, businessTransaction) -> {
            if (StringUtils.isNotBlank(customerName)) {
                //如果客户名不为null,进行以下操作
                //如果客户名和主借人名称一致,将此条记录添加到tempMap中
                if (customerName.equals(businessTransaction.getSubject())) {
                    candidateBusinessTransactionMap.put(businessTransaction.getContractNo(), businessTransaction);
                }
                //将客户名与个人共借人和担保人对比,如果一致,将此记录添加到tempMap中
                businessTransaction.getPersonalCoBorrowerAndGuarantor().forEach(
                        personalCoBorrowerAndGuarantor -> {
                            if (customerName.equals(personalCoBorrowerAndGuarantor)) {
                                candidateBusinessTransactionMap.put(businessTransaction.getContractNo(), businessTransaction);
                            }
                        }
                );
                //将客户名与企业共借人和担保人对比,如果一致,将此记录添加到tempMap中
                businessTransaction.getCompanyCoBorrowerAndGuarantor().forEach(
                        companyCoBorrowerAndGuarantor -> {
                            if (customerName.equals(companyCoBorrowerAndGuarantor)) {
                                candidateBusinessTransactionMap.put(businessTransaction.getContractNo(), businessTransaction);
                            }
                        }
                );
            }
        });
    }

    /**
     * 具体比较还款日期和还款金额与计划还款日,计划还款金额是否在允许误差范围内,如果是添加到result中
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     * @param result                    非代扣还款信息可能对应的业务信息
     * @param businessTransaction       业务信息
     * @param repaymentPlan             还款计划
     */
    private static void concreteCompareDateAndAmount(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, TreeSet<BusinessTransaction> result, BusinessTransaction businessTransaction, RepaymentPlan repaymentPlan) {
        //实际还款日与计划还款日对比
        LocalDate repaymentTermDate = Utility.getLocalDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
        LocalDate planTermDate = Utility.getLocalDate(repaymentPlan.getPlanTermDate());
        //两个日期的差值
        long between = ChronoUnit.DAYS.between(repaymentTermDate, planTermDate);
        if (between >= -1 && between <= 1) {
            //如果日期差值在[-1,1]范围内,继续判断实际还款金额和计划还款金额的差值是否在[-1,1]范围内
            BigDecimal repaymentAmount = new BigDecimal(nonDeductionRepaymentInfo.getRepaymentAmount());
            BigDecimal planTotalAmount = new BigDecimal(repaymentPlan.getPlanTotalAmount());
            BigDecimal difference = planTotalAmount.setScale(2, BigDecimal.ROUND_HALF_UP).subtract(repaymentAmount);
            if (difference.compareTo(BigDecimal.valueOf(-1)) >= 0 && difference.compareTo(BigDecimal.ONE) <= 0) {
                //如果实际还款金额和计划还款金额的差值是否在[-1,1]范围内,添加到保存结果的result中
                result.add(businessTransaction);
            }
        } else {
            //如果逾期了,也直接添加到结果中,最后判断result中是否只有一条记录
            result.add(businessTransaction);
        }
    }


    /**
     * 将可能对应的业务信息放入candidateBusinessTransactionMap中
     *
     * @param candidateBusinessTransactionMap 非代扣还款数据的可能对应业务的合同编号与业务信息的映射
     * @param associatePayer                  关联还款人信息
     */
    private static void putPossibleBusinessTransaction(Map<String, BusinessTransaction> candidateBusinessTransactionMap, AssociatePayer associatePayer) {
        BusinessTransaction businessTransaction = new BusinessTransaction();
        businessTransaction.setDateId(associatePayer.getDateId());
        businessTransaction.setContractNo(associatePayer.getContractNo());
        candidateBusinessTransactionMap.put(associatePayer.getContractNo(), businessTransaction);
    }


}
