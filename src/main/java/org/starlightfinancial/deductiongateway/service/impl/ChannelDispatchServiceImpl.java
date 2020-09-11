package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategyContext;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private LimitManagerRepository limitManagerRepository;

    @Autowired
    private AccountManagerRepository accountManagerRepository;

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
    public Message sign(AccountManager accountManager, String channel) {
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
        for (MortgageDeduction mortgageDeduction : list) {
            //split 方法会判断channel是否为空,这里不需要先判断
            Map<String, List<MortgageDeduction>> split = split(mortgageDeduction, channel);
            if (split.size() == 1) {
                String handlingChargeLowestChannel = split.keySet().iterator().next();
                List<MortgageDeduction> mortgageDeductions = split.get(handlingChargeLowestChannel);
                //可能存在不同的渠道进行代扣同一条记录,服务费与本息通过不同渠道代扣.对拆分后的list的代扣渠道分组,同一代扣渠道统一进行代扣
                Map<String, List<MortgageDeduction>> collect = mortgageDeductions.stream().collect(Collectors.groupingBy(MortgageDeduction::getChannel));
                for (Map.Entry<String, List<MortgageDeduction>> entry : collect.entrySet()) {
                    OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(entry.getKey());
                    operationStrategy.pay(entry.getValue());
                }
                mortgageDeductionRepository.save(mortgageDeductions);
            } else {
                //如果所有渠道都不支持此银行,给出提示
                mortgageDeduction.setErrorResult("暂无支持此银行的代扣渠道");
                mortgageDeductionRepository.save(mortgageDeduction);
            }

        }
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
        } else {
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

    /**
     * 如果没有指定代扣渠道,计算花费最少的代扣渠道来拆分.如果指定了代扣渠道,使用指定的代扣渠道拆分
     *
     * @param mortgageDeduction 待拆分的代扣记录
     * @param channel           指定的代扣渠道
     * @return 返回结果, 包含了代扣渠道以及拆分出来的代扣记录
     */
    @Override
    public Map<String, List<MortgageDeduction>> split(MortgageDeduction mortgageDeduction, String channel) {
        HashMap<BigDecimal, List<MortgageDeduction>> candidateMap = new HashMap<>(10);
        //首先查询支持这个银行的代扣渠道,然后剔除当前卡号不支持的快捷支付,再用剩下的代扣渠道各自拆分
        List<LimitManager> limitManagers = getAllowLimitManagers(mortgageDeduction);
        if (StringUtils.isNotBlank(channel)) {
            //如果指定了代扣渠道,使用它进行拆分,但是可能存在指定的代扣渠道不支持当前卡所属银行
            OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
            LimitManager limitManager = limitManagerRepository.findByBankCodeAndEnabledAndChannel(mortgageDeduction.getParam1(), ConstantsEnum.SUCCESS.getCode(), channel);
            if (Objects.isNull(limitManager)) {
                throw new UnsupportedOperationException("代扣记录合同号[" + mortgageDeduction.getContractNo() + "]指定的代扣渠道[" + channel + "]不支持当前银行卡所属银行代扣");
            }
            operationStrategy.splitWithSingleFeeType(candidateMap, mortgageDeduction, limitManager, limitManagers);
        } else {
            //如果没有指定代扣渠道,对每个渠道应用拆分方法
            limitManagers.forEach(limitManager -> {
                OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(limitManager.getChannel());
                operationStrategy.splitWithSingleFeeType(candidateMap, mortgageDeduction, limitManager, limitManagers);
            });
        }

        //获取手续费最小的渠道
        Optional<BigDecimal> min = candidateMap.keySet().stream().min(BigDecimal::compareTo);
        List<MortgageDeduction> mortgageDeductions = candidateMap.get(min.get());
        //将代扣渠道使用逗号分隔,如"代扣渠道1,代扣渠道2"如果是一个渠道只会有"代扣渠道1"
        String deductionChannel = mortgageDeductions.stream().map(MortgageDeduction::getChannel).distinct().collect(Collectors.joining(","));
        HashMap<String, List<MortgageDeduction>> deductionChannelEnumListHashMap = new HashMap<>(4);
        deductionChannelEnumListHashMap.put(deductionChannel, mortgageDeductions);
        return deductionChannelEnumListHashMap;
    }


    /**
     * 通过银行卡所属银行获取允许的代扣渠道
     *
     * @param mortgageDeduction 代扣信息
     * @return 代扣渠道
     */
    private List<LimitManager> getAllowLimitManagers(MortgageDeduction mortgageDeduction) {
        List<LimitManager> limitManagers = limitManagerRepository.findByBankCodeAndEnabled(mortgageDeduction.getParam1(), ConstantsEnum.SUCCESS.getCode());
        AccountManager accountManager = accountManagerRepository.findByContractNoAndAccountAndAccountNameAndCertificateNo(mortgageDeduction.getContractNo(),
                mortgageDeduction.getParam3(), mortgageDeduction.getCustomerName(), mortgageDeduction.getParam6());
        if (Objects.nonNull(accountManager)) {
            //判断银行卡号是否绑定快捷支付,来决定是否要删除快捷支付的代扣渠道
            if (!StringUtils.equals(String.valueOf(accountManager.getUnionpayIsSigned()), ConstantsEnum.SUCCESS.getCode())) {
                limitManagers.removeIf(limitManager -> StringUtils.equals(limitManager.getChannel(), DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode()));
            }
            if (!StringUtils.equals(String.valueOf(accountManager.getBaofuIsSigned()), ConstantsEnum.SUCCESS.getCode())) {
                limitManagers.removeIf(limitManager -> StringUtils.equals(limitManager.getChannel(), DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getCode()));
            }
            if (!StringUtils.equals(String.valueOf(accountManager.getChinaPayClearNetIsSigned()), ConstantsEnum.SUCCESS.getCode())) {
                limitManagers.removeIf(limitManager -> StringUtils.equals(limitManager.getChannel(), DeductionChannelEnum.CHINA_PAY_CLEAR_NET_QUICK.getCode()));
            }
            if (!StringUtils.equals(String.valueOf(accountManager.getPingAnCommercialEntrustIsSigned()), ConstantsEnum.SUCCESS.getCode())) {
                limitManagers.removeIf(limitManager -> StringUtils.equals(limitManager.getChannel(), DeductionChannelEnum.PING_AN_COMMERCIAL_ENTRUST.getCode()));
            }
        }
        return limitManagers;
    }

    /**
     * 获取手续费最少的渠道
     *
     * @param id 记录主键
     * @return
     */
    @Override
    public Message getHandlingChargeLowestChannel(Integer id) {
        Message message;
        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findOne(id);
        Map<String, List<MortgageDeduction>> split = split(mortgageDeduction, null);
        if (split.size() == 1) {
            message = Message.success();
            String handlingChargeLowestChannel = split.keySet().iterator().next();
            //代扣渠道目前最多有两个,本息渠道与服务费渠道
            String[] channels = handlingChargeLowestChannel.split(",");
            String msg;
            if (channels.length > 1) {
                msg = "手续费最少的代扣方式如下,本息:" + DeductionChannelEnum.getDescByCode(channels[0]) + ",服务费:" + DeductionChannelEnum.getDescByCode(channels[1]);
            } else {
                msg = "手续费最少渠道:" + DeductionChannelEnum.getDescByCode(split.keySet().iterator().next());
            }
            message.setData(msg);
        } else {
            message = Message.fail("暂无手续费最少渠道");
        }
        return message;
    }

    /**
     * 获取启用的支持当前记录银行的的渠道
     *
     * @param id 记录主键
     * @return
     */
    @Override
    public Message getEnabledChannel(Integer id) {
        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findOne(id);
        List<LimitManager> byBankCodeAndEnabled = limitManagerRepository.findByBankCodeAndEnabled(mortgageDeduction.getParam1(), ConstantsEnum.SUCCESS.getCode());
        Message message = Message.success();
        message.setData(byBankCodeAndEnabled.stream().map(LimitManager::getChannel).collect(Collectors.toList()));
        return message;
    }

    /**
     * 注册账号
     *
     * @param id      accountManager主键
     * @param channel 渠道
     * @return
     */
    @Override
    public Message registration(Integer id, String channel) {
        OperationStrategy operationStrategy = operationStrategyContext.getOperationStrategy(channel);
        Message message;
        if (operationStrategy != null) {
            message = operationStrategy.registration(id);
        } else {
            message = Message.fail("渠道信息未配置");
        }
        return message;
    }


}
