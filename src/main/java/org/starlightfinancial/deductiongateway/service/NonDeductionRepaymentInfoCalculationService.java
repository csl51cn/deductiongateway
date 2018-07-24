package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.AssociatePayer;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentPlan;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentPlanRepository;
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
        ConcurrentHashMap<String, BusinessTransaction> tempMap = new ConcurrentHashMap<>(16);
        //获取缓存中的所有业务信息
        Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();

        //通过非代扣还款数据的客户名称对比在业务系统中的借款人名称,个人共借人担保人名称,企业共借人担保人名称,如果有相同的,将那条业务信息添加到tempMap中,以非代扣还款数据为key,业务信息为value
        compareCoBorrowerAndGuarantor(customerName, tempMap, businessTransactionCacheMap);

        //通过非代扣还款数据的客户名称与关联还款人信息中的关联还款人1,2,3,4对比,如果有相同的,将那条业务信息添加到tempMap中,以非代扣还款数据为key,业务信息为value
        List<AssociatePayer> allAssociatePayer = Collections.synchronizedList(associatePayerService.findAll());
        allAssociatePayer.parallelStream().forEach(associatePayer -> {
            if (customerName != null) {
                if (customerName.equals(associatePayer.getPayer1())) {
                    putPossibleBusinessTransaction(tempMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer2())) {
                    putPossibleBusinessTransaction(tempMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer3())) {
                    putPossibleBusinessTransaction(tempMap, associatePayer);
                }
                if (customerName.equals(associatePayer.getPayer4())) {
                    putPossibleBusinessTransaction(tempMap, associatePayer);
                }
            }
        });

        //获得了所有可能对应的业务信息,进行最后的判断,判断还款日期和还款金额是否在允许的误差范围内
        List<BusinessTransaction> businessTransactions = Collections.synchronizedList(new ArrayList<>(tempMap.values()));
        TreeSet<BusinessTransaction> result = new TreeSet<>();
        compareDateAndAmount(nonDeductionRepaymentInfo, businessTransactions, result);


        //最后判断result中有多少业务信息,如果大小不为1,表示未查询到匹配的业务信息,不做出更改
        if (result.size() == 1) {
            nonDeductionRepaymentInfo.setDateId(result.first().getDateId());
            nonDeductionRepaymentInfo.setContractNo(result.first().getContractNo());
            nonDeductionRepaymentInfo.setIsIntegrated(String.valueOf(1));
        }
    }

    /**
     * 比较还款日期和还款金额是否在允许误差范围内,如果是添加到result中
     *
     * @param nonDeductionRepaymentInfo 非代扣还款信息
     * @param businessTransactions      业务信息
     * @param result                    可能匹配的业务信息
     */
    private static void compareDateAndAmount(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, List<BusinessTransaction> businessTransactions, TreeSet<BusinessTransaction> result) {
        //与计划还款信息中的还款日期和还款金额对比,缩小范围,还款日期±一天,还款金额±1元
        businessTransactions.parallelStream().forEach(businessTransaction -> {
            Integer codeByDesc = RepaymentTypeEnum.getCodeByDesc(nonDeductionRepaymentInfo.getRepaymentType());
            if (codeByDesc != null) {
                //获取还款类别代码不为null,查询对应还款类别的未结清的期数最小的还款计划信息
                RepaymentPlan repaymentPlan = repaymentPlanRepository.findFirstByPlanTypeIdAndStatusOrderByIdAsc(codeByDesc, "0");
                concreteCompareDateAndAmount(nonDeductionRepaymentInfo, result, businessTransaction, repaymentPlan);

            } else {
                //获取还款类别代码为null,查询所有还款类别的计划还款信息
                //本息
                RepaymentPlan repaymentPlan = repaymentPlanRepository.findFirstByPlanTypeIdAndStatusOrderByIdAsc(RepaymentTypeEnum.PRINCIPAL_AND_INTEREST.getCode(), "0");
                concreteCompareDateAndAmount(nonDeductionRepaymentInfo, result, businessTransaction, repaymentPlan);
                //服务费
                repaymentPlan = repaymentPlanRepository.findFirstByPlanTypeIdAndStatusOrderByIdAsc(RepaymentTypeEnum.SERVICE_FEE.getCode(), "0");
                concreteCompareDateAndAmount(nonDeductionRepaymentInfo, result, businessTransaction, repaymentPlan);
                //调查评估费
                repaymentPlan = repaymentPlanRepository.findFirstByPlanTypeIdAndStatusOrderByIdAsc(RepaymentTypeEnum.EVALUATION_FEE.getCode(), "0");
                concreteCompareDateAndAmount(nonDeductionRepaymentInfo, result, businessTransaction, repaymentPlan);
            }

        });
    }

    /**
     * 比较非代扣还款数据中的客户名称和共借人担保人以获取匹配的业务信息
     *
     * @param customerName                客户名称
     * @param tempMap                     可能对应业务的合同编号与业务信息的映射
     * @param businessTransactionCacheMap 所有业务信息
     */
    private static void compareCoBorrowerAndGuarantor(String customerName, ConcurrentHashMap<String, BusinessTransaction> tempMap, Map<String, BusinessTransaction> businessTransactionCacheMap) {
        businessTransactionCacheMap.forEach((contractNo, businessTransaction) -> {
            if (customerName != null) {
                //如果客户名不为null,进行以下操作
                //如果客户名和主借人名称一致,将此条记录添加到tempMap中
                if (customerName.equals(businessTransaction.getSubject())) {
                    tempMap.put(businessTransaction.getContractNo(), businessTransaction);
                }
                //将客户名与个人共借人和担保人对比,如果一致,将此记录添加到tempMap中
                businessTransaction.getPersonalCoBorrowerAndGuarantor().parallelStream().forEach(
                        personalCoBorrowerAndGuarantor -> {
                            if (customerName.equals(personalCoBorrowerAndGuarantor)) {
                                tempMap.put(businessTransaction.getContractNo(), businessTransaction);
                            }
                        }
                );
                //将客户名与企业共借人和担保人对比,如果一致,将此记录添加到tempMap中
                businessTransaction.getCompanyCoBorrowerAndGuarantor().parallelStream().forEach(
                        companyCoBorrowerAndGuarantor -> {
                            if (customerName.equals(companyCoBorrowerAndGuarantor)) {
                                tempMap.put(businessTransaction.getContractNo(), businessTransaction);
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
        }
    }


    /**
     * 将可能对应的业务信息放入tempMap中
     *
     * @param tempMap        非代扣还款数据的可能对应业务的合同编号与业务信息的映射
     * @param associatePayer 关联还款人信息
     */
    private static void putPossibleBusinessTransaction(ConcurrentHashMap<String, BusinessTransaction> tempMap, AssociatePayer associatePayer) {
        BusinessTransaction businessTransaction = new BusinessTransaction();
        businessTransaction.setDateId(associatePayer.getDateId());
        businessTransaction.setContractNo(associatePayer.getContractNo());
        tempMap.put(associatePayer.getContractNo(), businessTransaction);
    }


}
