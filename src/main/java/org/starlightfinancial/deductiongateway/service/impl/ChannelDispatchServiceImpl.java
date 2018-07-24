package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategyContext;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 各渠道签约状态查询, 签约短信发送, 签约, 支付, 支付状态查询等操作管理Service 接口实现
 * @date: Created in 2018/6/4 16:07
 * @Modified By:
 */
@Service
public class ChannelDispatchServiceImpl implements ChannelDispatchService {
    @Autowired
    private OperationStrategyContext operationStrategyContext;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;


    /**
     * 查询是否签约
     *
     * @param id      记录id
     * @param channel 渠道
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer id, String channel) {
        OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
        Message message;
        if (operationStrategy != null) {
            message = operationStrategy.queryIsSigned(id);
        } else {
            message = Message.fail("渠道信息未配置");
        }
        return message;
    }

    /**
     * 发送签约短信码
     *
     * @param accountManager 卡号相关信息
     * @param channel        渠道
     * @return 返回包含短信发送结果的Message对象
     */
    @Override
    public Message sendSignSmsCode(AccountManager accountManager, String channel) {
        OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
        Message message;
        if (operationStrategy != null) {
            message = operationStrategy.sendSignSmsCode(accountManager);
        } else {
            message = Message.fail("渠道信息未配置");
        }
        return message;
    }

    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @param channel        渠道
     * @return 返回签约结果的Message对象
     */
    @Override
    public Message Sign(AccountManager accountManager, String channel) {
        OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
        Message message;
        if (operationStrategy != null) {
            message = operationStrategy.sign(accountManager);
        } else {
            message = Message.fail("渠道信息未配置");
        }
        return message;
    }

    /**
     * 执行代扣
     *
     * @param list    需要进行代扣的List
     * @param channel 渠道
     * @return 返回包含代扣执行情况的Message对象
     */
    @Override
    public void doPay(List<MortgageDeduction> list, String channel) throws Exception {
        OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
        operationStrategy.pay(list);
    }

    /**
     * 查询代扣结果
     *
     * @param id 代扣记录id
     * @return 返回包含代扣查询结果Message对象
     */
    @Override
    public Message queryPayResult(Integer id) {
        Message message;
        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.getOne(id);
        if (StringUtils.equals(mortgageDeduction.getType(), "1")) {
            message = Message.fail("请先进行代扣操作,再查询代扣结果");
            return message;
        }else{
            String channel = mortgageDeduction.getChannel();
            OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
            if (operationStrategy != null) {
                message = operationStrategy.queryPayResult(mortgageDeduction);
            } else {
                message = Message.fail("渠道信息未配置");
            }
        }
        return message;
    }
}
