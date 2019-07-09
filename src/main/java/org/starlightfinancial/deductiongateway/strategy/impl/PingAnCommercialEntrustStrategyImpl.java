package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.config.PingAnConfig;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.FIFOCache;
import org.starlightfinancial.deductiongateway.utility.HessianProxyFactoryUtils;
import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.yqb.request.*;
import org.starlightfinancial.rpc.hessian.entity.yqb.response.*;
import org.starlightfinancial.rpc.hessian.service.yqb.PingAnBaseService;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 平安商委代扣
 * @date: Created in 2019/6/27 13:38
 * @Modified By:
 */
@Service("0009")
public class PingAnCommercialEntrustStrategyImpl implements OperationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PingAnCommercialEntrustStrategyImpl.class);

    /**
     * 缓存签约订单号与AccountManage的映射
     */
    public static final FIFOCache<String, Integer> SIGN_CACHE = new FIFOCache<>(150);
    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private PingAnConfig pingAnConfig;

    @Autowired
    private BeanConverter beanConverter;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    /**
     * 查询是否签约,如果没有签约自动发送验证码
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer id) {
        Message message;
        AccountManager accountManager = accountManagerRepository.getOne(id);
        try {
            //商委签约查询,如果没有签约自动发送短信,如果已经签约,返回签约的bankId.
            PingAnBaseService tx050Service = HessianProxyFactoryUtils.getHessianClientBean(PingAnBaseService.class, pingAnConfig.getSignStatusUrl());
            Tx050Request request = beanConverter.transToTx050Req(accountManager);
            LOGGER.debug("平安商委签约050请求:{}", JSONObject.toJSONString(request));
            RequestResult requestResult = tx050Service.doBusiness(request);
            LOGGER.debug("平安商委签约050返回:{}", JSONObject.toJSONString(requestResult));
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                Tx050Response tx050Response = (Tx050Response) requestResult.getResult().get(0);
                message = Message.success();
                message.setData(tx050Response.getTraceNo());
            } else if (StringUtils.equals(requestResult.getErrorCode(), "0156")) {
                Tx050Response tx050Response = (Tx050Response) requestResult.getResult().get(0);
                //已绑卡,无需发送短信,提示完成签约
                accountManager.setPingAnCommercialEntrustIsSigned(ConstantsEnum.SUCCESS.getCode());
                accountManager.setPingAnCommercialEntrustProtocolNo(tx050Response.getBankId());
                accountManagerRepository.saveAndFlush(accountManager);
                message = Message.success();
            } else {
                message = Message.fail("平安商委签约异常");
            }
        } catch (Exception e) {
            LOGGER.error("平安商委签约异常:账户id:[" + accountManager.getId() + "],姓名:[" + accountManager.getAccountName() + "]", e);
            message = Message.fail("平安商委签约异常");
        }
        return message;
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
        Message message;
        try {
            PingAnBaseService tx047Service = HessianProxyFactoryUtils.getHessianClientBean(PingAnBaseService.class, pingAnConfig.getSignUrl());
            Tx047Request request = beanConverter.transToTx047Req(accountManager);

            LOGGER.debug("平安商委签约短信验证047请求:{}", JSONObject.toJSONString(request));
            RequestResult requestResult = tx047Service.doBusiness(request);
            LOGGER.debug("平安商委签约短信验证047返回:{}", JSONObject.toJSONString(requestResult));
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                AccountManager accountManagerById = accountManagerRepository.getOne(accountManager.getId());
                Tx047Response tx047Response = (Tx047Response) requestResult.getResult().get(0);
                accountManagerById.setPingAnCommercialEntrustProtocolNo(tx047Response.getBankId());
                accountManagerById.setPingAnCommercialEntrustIsSigned(ConstantsEnum.SUCCESS.getCode());
                accountManagerRepository.saveAndFlush(accountManagerById);
                message = Message.success();
                SIGN_CACHE.put(request.getMerchantSeqNo(), accountManagerById.getId());
            } else {
                message = Message.fail("平安商委签约短信验证异常");
            }
        } catch (Exception e) {
            LOGGER.error("平安商委签约短信验证异常:账户id:[" + accountManager.getId() + "],姓名:[" + accountManager.getAccountName() + "]", e);
            message = Message.fail("平安商委签约短信验证异常");
        }
        return message;
    }

    /**
     * 代扣
     *
     * @param mortgageDeductions mortgageDeduction列表
     * @throws Exception 执行代扣异常
     */
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {
        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {
            Tx001Request request = beanConverter.transToTx001Req(mortgageDeduction);
            mortgageDeduction.setOrdId(request.getMercOrderNo());
            mortgageDeduction.setMerId(request.getMerchantId());
            mortgageDeduction.setCuryId("156");
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.PING_AN_COMMERCIAL_ENTRUST.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.PING_AN_COMMERCIAL_ENTRUST.getCode());
            mortgageDeduction.setPayTime(new Date());
            LOGGER.debug("平安合壹付代扣请求:{}", JSONObject.toJSONString(request));
            PingAnBaseService tx001Service = HessianProxyFactoryUtils.getHessianClientBean(PingAnBaseService.class, pingAnConfig.getPayUrl());
            RequestResult requestResult = tx001Service.doBusiness(request);
            LOGGER.debug("平安合壹付代扣返回:{}", JSONObject.toJSONString(requestResult));
            Tx001Response response = (Tx001Response) requestResult.getResult().get(0);
            mortgageDeduction.setErrorResult("收单成功");
            mortgageDeduction.setResult(response.getRespCode());
            calculateHandlingCharge(mortgageDeduction);
            if (!StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //如果返回不是0000,收单未成功
                mortgageDeduction.setIssuccess(ConstantsEnum.FAIL.getCode());
            }
        }
    }

    /**
     * 查询代扣结果
     *
     * @param mortgageDeduction 代扣记录
     * @return 返回包含代扣查询结果Message对象
     */
    @Override
    public Message queryPayResult(MortgageDeduction mortgageDeduction) {
        Tx005Request request = new Tx005Request();
        request.setMerchantId(pingAnConfig.getMerchantId());
        request.setMercOrderNo(mortgageDeduction.getOrdId());
        Message message;
        try {
            PingAnBaseService tx005Service = HessianProxyFactoryUtils.getHessianClientBean(PingAnBaseService.class, pingAnConfig.getQueryResultUrl());
            RequestResult requestResult = tx005Service.doBusiness(request);
            String errorCode = requestResult.getErrorCode();
            Tx005Response response = (Tx005Response) requestResult.getResult().get(0);
            if (StringUtils.equals(errorCode, RsbCodeEnum.ERROR_CODE_01.getCode())) {
                String orderStatus = response.getOrderStatus();
                //01或06 业务处理中,不用设置状态   02 04 05 与退款有关,按业务来分不会出现
                if (StringUtils.equals(orderStatus, PingAnOrderStatusEnum.STATUS00.getStatus())) {
                    //00 支付成功
                    mortgageDeduction.setIssuccess(ConstantsEnum.SUCCESS.getCode());
                    mortgageDeduction.setErrorResult("交易成功");
                    mortgageDeduction.setResult(requestResult.getErrorCode());
                    calculateHandlingCharge(mortgageDeduction);
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                } else if (StringUtils.equals(orderStatus, PingAnOrderStatusEnum.STATUS03.getStatus())
                        || StringUtils.equals(orderStatus, PingAnOrderStatusEnum.STATUS08.getStatus())
                        || StringUtils.equals(orderStatus, PingAnOrderStatusEnum.STATUS09.getStatus())) {
                    // 03或08或09 支付失败
                    mortgageDeduction.setIssuccess(ConstantsEnum.FAIL.getCode());
                    mortgageDeduction.setErrorResult(response.getErrMsg());
                    mortgageDeduction.setResult(response.getErrCode());
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                }
            } else {
                //响应码0021-查询信息不存在，因为可能存在查询请求比扣款请求先到达壹钱包的情况，所以，0021错误码不应断定失败
                if (!StringUtils.equals(errorCode, "0021")) {
                    mortgageDeduction.setIssuccess(ConstantsEnum.FAIL.getCode());
                    mortgageDeduction.setErrorResult(response.getErrMsg());
                    mortgageDeduction.setResult(response.getErrCode());
                }
            }
            message = Message.success();
        } catch (Exception e) {
            LOGGER.error("平安商委代扣结果查询异常,记录id:" + mortgageDeduction.getId() + ",账户名:" + mortgageDeduction.getCustomerName(), e);
            message = Message.fail("代扣状态查询异常");
        }
        return message;
    }

    /**
     * 计算并设置手续费
     *
     * @param mortgageDeduction 代扣记录
     */
    @Override
    public void calculateHandlingCharge(MortgageDeduction mortgageDeduction) {
        mortgageDeduction.setHandlingCharge(pingAnConfig.getCharge());
    }

    /**
     * 平安域账户注册
     *
     * @param id 代扣系统的账户信息id
     * @return 注册结果
     */
    @Override
    public Message registration(Integer id) {
        AccountManager accountManager = accountManagerRepository.getOne(id);
        String bankCode = PingAnBankCodeEnum.getCodeByBankName(accountManager.getBankName());
        if (Objects.isNull(bankCode)) {
            return Message.fail("平安商委不支持当前银行签约");
        }
        Message message;
        try {
            //域账号注册,如果没有注册自动注册,如果已经注册会返回用户平安付id
            PingAnBaseService registrationService = HessianProxyFactoryUtils.getHessianClientBean(PingAnBaseService.class, pingAnConfig.getRegistrationUrl());
            TxRegistrationRequest txRegistrationRequest = beanConverter.transToTxRegistrationReq(accountManager);
            LOGGER.debug("平安域账户注册请求:{}", JSONObject.toJSONString(txRegistrationRequest));
            RequestResult requestResult = registrationService.doBusiness(txRegistrationRequest);
            LOGGER.debug("平安域账户注册返回:{}", JSONObject.toJSONString(requestResult));
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                TxRegistrationResponse registrationResponse = (TxRegistrationResponse) requestResult.getResult().get(0);
                accountManager.setPingAnCustomerId(registrationResponse.getCustomerId());
                accountManagerRepository.saveAndFlush(accountManager);
                message = Message.success();
            } else {
                message = Message.fail("平安域账户注册异常");
            }
        } catch (Exception e) {
            LOGGER.error("平安域账户注册异常:账户id:[" + accountManager.getId() + "],姓名:[" + accountManager.getAccountName() + "]", e);
            message = Message.fail("平安域账户注册异常");
        }

        return message;
    }
}
