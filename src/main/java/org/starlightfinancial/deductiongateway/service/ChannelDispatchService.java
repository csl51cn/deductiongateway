package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 各渠道签约状态查询, 签约短信发送, 签约, 支付, 支付状态查询等操作管理Service接口
 * @date: Created in 2018/6/4 16:06
 * @Modified By:
 */
public interface ChannelDispatchService {


    /**
     * 查询是否签约
     *
     * @param id      记录id
     * @param channel 渠道
     * @return 返回包含查询结果的Message对象
     */
    Message queryIsSigned(Integer id, String channel);

    /**
     * 发送签约短信码
     *
     * @param accountManager 卡号相关信息
     * @param channel        渠道
     * @return 返回包含短信发送结果的Message对象
     */
    Message sendSignSmsCode(AccountManager accountManager, String channel);

    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @param channel        渠道
     * @return 返回签约结果的Message对象
     */
    Message Sign(AccountManager accountManager, String channel);

    /**
     * 执行代扣
     *
     * @param list    需要进行代扣的List
     * @param channel 渠道
     * @return 返回包含代扣执行情况的Message对象
     */
    Message doPay(List<MortgageDeduction> list, String channel);


    /**
     * 查询代扣结果
     *
     * @param id      代扣记录id
     * @param channel 渠道
     * @return 返回包含代扣查询结果Message对象
     */
    Message queryPayResult(Integer id, String channel);
}
