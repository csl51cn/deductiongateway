package org.starlightfinancial.deductiongateway.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.utility.Constant;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * @author: Senlin.Deng
 * @Description: 操作策略接口
 * @date: Created in 2018/6/4 11:05
 * @Modified By:
 */
public interface OperationStrategy {


    /**
     * 查询是否签约
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    Message queryIsSigned(Integer id);


    /**
     * 发送短信验证码
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回包含短信发送结果的Message对象
     */
    Message sendSignSmsCode(AccountManager accountManager);


    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回签约结果的Message对象
     */
    Message sign(AccountManager accountManager);


    /**
     * 代扣
     *
     * @param mortgageDeductions mortgageDeduction列表
     * @throws Exception 执行代扣异常
     */
    void pay(List<MortgageDeduction> mortgageDeductions) throws Exception;


    /**
     * 查询代扣结果
     *
     * @param mortgageDeduction 代扣记录
     * @return 返回包含代扣查询结果Message对象
     */
    Message queryPayResult(MortgageDeduction mortgageDeduction);

    /**
     * 计算并设置手续费
     *
     * @param mortgageDeduction 代扣记录
     */
    void calculateHandlingCharge(MortgageDeduction mortgageDeduction);

    /**
     * 根据单笔限额拆分记录
     *
     * @param candidateMap      候选的拆分方案
     * @param mortgageDeduction 代扣信息
     * @param limitManager      限额
     * @return
     */
    default void split(Map<BigDecimal, List<MortgageDeduction>> candidateMap, MortgageDeduction mortgageDeduction, LimitManager limitManager) {
        //由于原始mortgageDeduction可能还有渠道需要使用,不能直接对原始的mortgageDeduction对象修改属性,
        MortgageDeduction copyMortgageDeduction = mortgageDeduction.cloneSelf();
        List<MortgageDeduction> result = new ArrayList<>();
        copyMortgageDeduction.setChannel(limitManager.getChannel());
        BigDecimal drawDownBxAmount = copyMortgageDeduction.getSplitData1();
        BigDecimal drawDownFwfAmount = copyMortgageDeduction.getSplitData2();
        BigDecimal totalAmount = drawDownBxAmount.add(drawDownFwfAmount);
        BigDecimal singleLimit = limitManager.getSingleLimit();
        BigDecimal bxAmount;
        BigDecimal fwfAmount;

        while (totalAmount.compareTo(singleLimit) > 0) {
            if (drawDownFwfAmount.compareTo(singleLimit) >= 0) {
                bxAmount = BigDecimal.ZERO;
                fwfAmount = singleLimit;
            } else {
                bxAmount = singleLimit.subtract(drawDownFwfAmount);
                fwfAmount = drawDownFwfAmount;
            }

            MortgageDeduction newMortgageDeduction = new MortgageDeduction();
            BeanUtils.copyProperties(copyMortgageDeduction, newMortgageDeduction);
            newMortgageDeduction.setId(null);
            newMortgageDeduction.setSplitData1(bxAmount);
            newMortgageDeduction.setSplitData2(fwfAmount);
            calculateHandlingCharge(newMortgageDeduction);

            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
            drawDownFwfAmount = drawDownFwfAmount.subtract(fwfAmount);
            totalAmount = drawDownBxAmount.add(drawDownFwfAmount);
            copyMortgageDeduction.setSplitData1(drawDownBxAmount);
            copyMortgageDeduction.setSplitData2(drawDownFwfAmount);
            result.add(newMortgageDeduction);
        }
        calculateHandlingCharge(copyMortgageDeduction);
        result.add(copyMortgageDeduction);

        BigDecimal handlingCharge = result.stream().map(MortgageDeduction::getHandlingCharge).reduce(BigDecimal.ZERO, BigDecimal::add);
        candidateMap.put(handlingCharge, result);

    }

    /**
     * 根据限额拆分代扣记录
     *
     * @param candidateMap      候选的拆分方案
     * @param mortgageDeduction 代扣信息
     * @param limitManager      限额
     * @param limitManagers     支持当前代扣卡的所有代扣渠道
     */
    default void split(Map<BigDecimal, List<MortgageDeduction>> candidateMap, MortgageDeduction mortgageDeduction, LimitManager limitManager, List<LimitManager> limitManagers) {
        //判断是否有分账费用2并且入账账户是悦至渝
        boolean isYuezhiyu = mortgageDeduction.getSplitData2().compareTo(BigDecimal.ZERO) > 0 && StringUtils.equals(mortgageDeduction.getTarget(), "悦至渝");
        if (isYuezhiyu && !StringUtils.equals(limitManager.getSupportYuezhiyu(), Constant.CHECK_SUCCESS)) {
            //如果当前服务费入账账户是悦至渝并且当前代扣渠道不支持悦至渝分账,使用多个渠道代扣.
            //首先本息使用当前代扣渠道进行代扣,然后服务费使用支持悦至渝的代扣渠道进行代扣

            //由于原始mortgageDeduction可能还有渠道需要使用,不能直接对原始的mortgageDeduction对象修改属性
            MortgageDeduction copyMortgageDeduction = mortgageDeduction.cloneSelf();
            List<MortgageDeduction> result = new ArrayList<>();

            copyMortgageDeduction.setChannel(limitManager.getChannel());
            MortgageDeduction serviceFee = new MortgageDeduction();
            BeanUtils.copyProperties(copyMortgageDeduction, serviceFee);
            //copyMortgageDeduction将服务费置零
            copyMortgageDeduction.setSplitData2(BigDecimal.ZERO);
            //将serviceFee的id置空,避免覆盖本息信息
            serviceFee.setId(null);
            //serviceFee将本息金额置零
            serviceFee.setSplitData1(BigDecimal.ZERO);
            List<LimitManager> supportYuezhiyuLimitManagers = limitManagers.stream().filter(l -> StringUtils.equals(l.getSupportYuezhiyu(), Constant.CHECK_SUCCESS)).collect(Collectors.toList());

            BigDecimal drawDownBxAmount = copyMortgageDeduction.getSplitData1();
            BigDecimal bxSingleLimit = limitManager.getSingleLimit();


            //本息拆分
            while (drawDownBxAmount.compareTo(bxSingleLimit) > 0) {
                MortgageDeduction newMortgageDeduction = new MortgageDeduction();
                BeanUtils.copyProperties(copyMortgageDeduction, newMortgageDeduction);
                newMortgageDeduction.setId(null);
                newMortgageDeduction.setSplitData1(bxSingleLimit);
                newMortgageDeduction.setSplitData2(BigDecimal.ZERO);
                calculateHandlingCharge(newMortgageDeduction);
                drawDownBxAmount = drawDownBxAmount.subtract(bxSingleLimit);
                copyMortgageDeduction.setSplitData1(drawDownBxAmount);
                result.add(newMortgageDeduction);
            }
            calculateHandlingCharge(copyMortgageDeduction);
            result.add(copyMortgageDeduction);

            //手续费与拆分方案映射Map
            TreeMap<BigDecimal, List<MortgageDeduction>> serviceFeeMap = new TreeMap<>();
            //服务费 map<手续费,拆分方案>
            supportYuezhiyuLimitManagers.forEach(serviceFeeLimitManager -> {
                ArrayList<MortgageDeduction> mortgageDeductions = new ArrayList<>();
                BigDecimal singleLimit = serviceFeeLimitManager.getSingleLimit();
                MortgageDeduction candidateServiceFee = new MortgageDeduction();
                BeanUtils.copyProperties(serviceFee, candidateServiceFee);
                BigDecimal drawDownFwfAmount = candidateServiceFee.getSplitData2();
                while (drawDownFwfAmount.compareTo(singleLimit) > 0) {
                    //如果服务费>单笔限额,新建一笔代扣记录,金额为单笔限额,将服务费-单笔限额继续循环直到≤单笔限额
                    MortgageDeduction newMortgageDeduction = new MortgageDeduction();
                    BeanUtils.copyProperties(candidateServiceFee, newMortgageDeduction);
                    newMortgageDeduction.setId(null);
                    newMortgageDeduction.setSplitData1(BigDecimal.ZERO);
                    newMortgageDeduction.setSplitData2(singleLimit);
                    calculateHandlingCharge(newMortgageDeduction);
                    drawDownFwfAmount = drawDownFwfAmount.subtract(singleLimit);
                    candidateServiceFee.setSplitData2(drawDownFwfAmount);
                    mortgageDeductions.add(newMortgageDeduction);
                }
                calculateHandlingCharge(candidateServiceFee);
                mortgageDeductions.add(candidateServiceFee);
                BigDecimal handlingCharge = mortgageDeductions.stream().map(MortgageDeduction::getHandlingCharge).reduce(BigDecimal.ZERO, BigDecimal::add);
                serviceFeeMap.put(handlingCharge, mortgageDeductions);
            });
            //按升序排序后的服务费手续费
            List<BigDecimal> collect = serviceFeeMap.keySet().stream().sorted().collect(Collectors.toList());
            //服务费手续费最低的代扣渠道
            List<MortgageDeduction> mortgageDeductions = serviceFeeMap.get(collect.get(0));
            result.addAll(mortgageDeductions);
            BigDecimal handlingCharge = result.stream().map(MortgageDeduction::getHandlingCharge).reduce(BigDecimal.ZERO, BigDecimal::add);
            candidateMap.put(handlingCharge, result);

        } else {
            //如果当前费用2入账账户不是悦至渝或者当前代扣渠道支持悦至渝分账,使用单个渠道代扣
            split(candidateMap, mortgageDeduction, limitManager);

        }

    }

    /**
     * 平安域账户注册
     *
     * @param id 代扣系统的账户信息id
     * @return 注册结果
     */
    default Message registration(Integer id) {
        return null;
    }


}
