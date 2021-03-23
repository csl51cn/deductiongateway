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
import org.starlightfinancial.deductiongateway.enums.RepaymentTypeEnum;
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


        //导入生成时间是从昨天到今天的非代扣还款数据,需要将拆分出来的还款记录的还款金额合并到被拆分的还款记录中,后面会去重
        List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoRepository.findByGmtModifiedGreaterThanEqualAndGmtModifiedLessThanEqualAndIsUploaded(yesterday, Utility.toMidNight(today), ConstantsEnum.SUCCESS.getCode());
        //获得拆分出来的非代扣还款记录map,键是id,值是记录
        Map<Long, NonDeductionRepaymentInfo> splitOutNonDeductionRepaymentInfoMap = Utility.deepCopy(nonDeductionRepaymentInfos).stream()
                .filter(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.getOriginalId() != null).collect(Collectors.toMap(NonDeductionRepaymentInfo::getId, Function.identity()));
        //获得被拆分的非代扣还款记录map,键是id,值是记录,首先获得被拆分的List
        List<NonDeductionRepaymentInfo> beSplitedNonDeductionRepaymentInfoList = nonDeductionRepaymentInfos.stream()
                .filter(nonDeductionRepaymentInfo -> nonDeductionRepaymentInfo.getOriginalId() == null).collect(Collectors.toList());
        //脱离Jpa管理,否则修改了被拆分记录的金额会自动更新到数据库中
        Map<Long, NonDeductionRepaymentInfo> beSplitedNonDeductionRepaymentInfoMap = Utility.deepCopy(beSplitedNonDeductionRepaymentInfoList).stream()
                .collect(Collectors.toMap(NonDeductionRepaymentInfo::getId, Function.identity()));

        HashMap<Long, NonDeductionRepaymentInfo> toBeSavedSplitOutNonDeductionRepaymentInfoMap = new HashMap<>(16);

        splitOutNonDeductionRepaymentInfoMap.forEach((id, nonDeductionRepaymentInfo) -> {
            //获得对应的被拆分的非代扣还款记录
            NonDeductionRepaymentInfo originalNonDeductionRepaymentInfo = beSplitedNonDeductionRepaymentInfoMap.get(nonDeductionRepaymentInfo.getOriginalId());
            //将拆分出来的记录的还款金额加到被拆分的记录的还款金额中合并成一条记录.
            //前端限制了被拆分的记录还款类型不能是取件费和诉讼费,也就是说取件费和诉讼费是单独还款回来或者拆分出来的是取件费和诉讼费
            //为方便生成财务凭证,有取件费时需要特殊处理,比如原记录本息金额10000,拆出100取件费,最后需要存在两条记录,一条还款金额是10000,一条是100的取件费
            //如果诉讼费是被拆分出来的,最后需要存在两条记录,比如原纪录本息金额10000,拆出100诉讼费,最后需要存在两条记录,一条还款金额是10000,一条是100的诉讼费
            originalNonDeductionRepaymentInfo.setRepaymentAmount(originalNonDeductionRepaymentInfo.getRepaymentAmount().add(nonDeductionRepaymentInfo.getRepaymentAmount()));
            if (StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(), RepaymentTypeEnum.PICK_UP_FEE.getDesc())
                    || StringUtils.equals(nonDeductionRepaymentInfo.getRepaymentType(), RepaymentTypeEnum.LITIGATION_CAST.getDesc())) {
                toBeSavedSplitOutNonDeductionRepaymentInfoMap.put(nonDeductionRepaymentInfo.getOriginalId(), nonDeductionRepaymentInfo);
            }
        });

        //将非代扣还款信息转换为还款信息,并添加到repaymentInfos中
        beSplitedNonDeductionRepaymentInfoMap.forEach((id, nonDeductionRepaymentInfo) -> {
            repaymentInfos.add(beanConverter.transToRepaymentInfo(nonDeductionRepaymentInfo));
            NonDeductionRepaymentInfo splitOutNonDeductionRepaymentInfo = toBeSavedSplitOutNonDeductionRepaymentInfoMap.get(id);
            if (Objects.nonNull(splitOutNonDeductionRepaymentInfo)) {
                repaymentInfos.add(beanConverter.transToRepaymentInfo(splitOutNonDeductionRepaymentInfo ));
            }
        });

        //去重,首先从数据中查询所有昨天导入的还款数据
        List<RepaymentInfo> existedRepaymentInfos = repaymentInfoRepository.findByGmtCreateGreaterThanEqualAndGmtCreateLessThanEqual(yesterday, Utility.toMidNight(today));
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
