package org.starlightfinancial.deductiongateway.strategy;

import org.springframework.beans.BeanUtils;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 平安域账户注册
     *
     * @param id 代扣系统的账户信息id
     * @return 注册结果
     */
    default Message registration(Integer id) {
        return null;
    }


}
