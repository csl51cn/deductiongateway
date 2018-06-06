package org.starlightfinancial.deductiongateway.strategy;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.util.List;

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
     * @param mortgageDeductions  mortgageDeduction列表
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

}
