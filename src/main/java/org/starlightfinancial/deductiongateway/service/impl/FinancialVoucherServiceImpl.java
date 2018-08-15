package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoRepository;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.remote.RepaymentInfoRepository;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.service.FinancialVoucherService;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: Senlin.Deng
 * @Description: 财务凭证管理service接口实现类
 * @date: Created in 2018/8/10 16:29
 * @Modified By:
 */
@Service
public class FinancialVoucherServiceImpl implements FinancialVoucherService {

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BeanConverter beanConverter;

    @Autowired
    private NonDeductionRepaymentInfoRepository nonDeductionRepaymentInfoRepository;

    @Autowired
    private RepaymentInfoRepository repaymentInfoRepository;

    /**
     * 导入昨天的还款数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importRepaymentData() throws IOException, ClassNotFoundException {
        //需要保存的还款信息List
        ArrayList<RepaymentInfo> repaymentInfos = new ArrayList<>();

        //导入还款日期是昨天的代扣成功的数据
        Date today = Utility.getDate(LocalDate.now());
        Date yesterday = Utility.addDay(today, -1);
        List<MortgageDeduction> mortgageDeductions = mortgageDeductionRepository.findByIssuccessAndPayTimeAfterAndPayTimeBefore(ConstantsEnum.SUCCESS.getCode(), yesterday, today);
        mortgageDeductions.forEach(mortgageDeduction -> repaymentInfos.addAll(beanConverter.transToRepaymentInfo(mortgageDeduction)));


        //导入还款日期是昨天的非代扣还款数据,需要将拆分出来的还款记录的还款金额合并到被拆分的还款记录中
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoRepository.findByRepaymentTermDateGreaterThanEqualAndRepaymentTermDateBefore(yesterday, today);
        //获得拆分出来的非代扣还款记录map,键是id,值是记录
        Map<Long, NonDeductionRepaymentInfo> splitOutNonDeductionRepaymentInfoMap = nonDeductionRepaymentInfos.stream()
                .filter(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.getOriginalId() != null).collect(Collectors.toMap(NonDeductionRepaymentInfo::getId, Function.identity()));
        //获得被拆分的非代扣还款记录map,键是id,值是记录,首先获得被拆分的List
        List<NonDeductionRepaymentInfo> beSplitedNonDeductionRepaymentInfoList = nonDeductionRepaymentInfos.stream()
                .filter(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.getOriginalId() == null).collect(Collectors.toList());
        //脱离Jpa管理,否则修改了被拆分记录的金额会自动更新到数据库中
        Map<Long, NonDeductionRepaymentInfo> beSplitedNonDeductionRepaymentInfoMap = Utility.deepCopy(beSplitedNonDeductionRepaymentInfoList).stream()
                .collect(Collectors.toMap(NonDeductionRepaymentInfo::getId, Function.identity()));

        splitOutNonDeductionRepaymentInfoMap.forEach((id, nonDeductionRepaymentInfo) -> {
            //获得对应的被拆分的非代扣还款记录
            NonDeductionRepaymentInfo originalNonDeductionRepaymentInfo = beSplitedNonDeductionRepaymentInfoMap.get(nonDeductionRepaymentInfo.getOriginalId());
            //将拆分出来的记录的还款金额加到被拆分的记录的还款金额中
            originalNonDeductionRepaymentInfo.setRepaymentAmount(originalNonDeductionRepaymentInfo.getRepaymentAmount().add(nonDeductionRepaymentInfo.getRepaymentAmount()));
        });
        //将非代扣还款信息转换为还款信息,并添加到repaymentInfos中
        beSplitedNonDeductionRepaymentInfoMap.forEach((id, nonDeductionRepaymentInfo) -> {
            repaymentInfos.add(beanConverter.transToRepaymentInfo(nonDeductionRepaymentInfo));
        });

        //去重,首先从数据中查询所有昨天的还款数据
        List<RepaymentInfo> existedRepaymentInfos = repaymentInfoRepository.findByRepaymentTermDateGreaterThanEqualAndRepaymentTermDateBefore(yesterday, today);
        Iterator<RepaymentInfo> iterator = repaymentInfos.iterator();
        while (iterator.hasNext()) {
            RepaymentInfo next = iterator.next();
            //根据原始是代扣和原始表的Id是否相同判断数据库中是否有相同的还款数据
            boolean anyMatch = existedRepaymentInfos.stream().anyMatch(existedRepaymentInfo ->
                    existedRepaymentInfo.getOriginalId().equals(next.getOriginalId()) && StringUtils.equals(existedRepaymentInfo.getIsDeduction(), next.getIsDeduction()));
            if (anyMatch) {
                iterator.remove();
            }
        }
        //入库
        repaymentInfoRepository.save(repaymentInfos);
    }

}
