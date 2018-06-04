package org.starlightfinancial.deductiongateway.strategy.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:  银联白名单代扣渠道操作策略
 * @date: Created in 2018/6/4 11:17
 * @Modified By:
 */
@Component("0003")
public class UnionPayWhiteListPayStrategy implements OperationStrategy {


    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;



    /**
     * 查询是否签约
     *
     * @param Id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer Id) {
        return null;
    }

    /**
     * 发送短信验证码
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回包含短信发送结果的Message对象
     */
    @Override
    public Message sendSignSmsCode(AccountManager accountManager) {
        return null;
    }

    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回签约结果的Message对象
     */
    @Override
    public Message sign(AccountManager accountManager) {
        return null;
    }

    /**
     * 代扣
     *
     * @param mortgageDeductions mortgageDeduction列表
     * @return 返回包含代扣执行情况的Message对象
     */
    @Override
    public Message pay(List<MortgageDeduction> mortgageDeductions) {
        return null;
    }

    /**
     * 查询代扣结果
     *
     * @param id 代扣记录id
     * @return 返回包含代扣查询结果Message对象
     */
    @Override
    public Message queryPayResult(Integer id) {
        return null;
    }
}
