package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.util.List;
import java.util.Map;

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
    Message sign(AccountManager accountManager, String channel);

    /**
     * 执行代扣
     *
     * @param list    需要进行代扣的List
     * @param channel 渠道
     * @return 返回包含代扣执行情况的Message对象
     */
    void doPay(List<MortgageDeduction> list, String channel) throws Exception;


    /**
     * 查询代扣结果
     *
     * @param id 代扣记录id
     * @return 返回包含代扣查询结果Message对象
     */
    Message queryPayResult(Integer id);

    /**
     * 如果没有指定代扣渠道,计算花费最少的代扣渠道来拆分.如果指定了代扣渠道,使用指定的代扣渠道拆分
     *
     * @param mortgageDeduction 待拆分的代扣记录
     * @param channel           指定的代扣渠道
     * @return 返回结果, 包含了代扣渠道以及拆分出来的代扣记录
     */
    Map<String, List<MortgageDeduction>> split(MortgageDeduction mortgageDeduction, String channel);

    /**
     * 获取手续费最少的渠道
     * @param id 记录主键
     * @return
     */
    Message getHandlingChargeLowestChannel(Integer id);
}
