package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.ChinaPayClearNetConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.ChinaPayClearNetBankCodeEnum;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HessianProxyFactoryUtils;
import org.starlightfinancial.deductiongateway.utility.XmlUtils;
import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.*;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2504Res;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2511Res;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2512Res;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2532Res;
import org.starlightfinancial.rpc.hessian.service.cpcn.BaseService;
import payment.api.util.GUIDGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付快捷支付
 * @date: Created in 2019/5/5 14:08
 * @Modified By:
 */
@Service("0007")
public class ChinaPayClearNetQuickStrategyImpl implements OperationStrategy {


    private static final Logger LOGGER = LoggerFactory.getLogger(ChinaPayClearNetQuickStrategyImpl.class);
    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private ChinaPayClearNetConfig chinaPayClearNetConfig;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BeanConverter beanConverter;

    /**
     * 查询是否签约
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message queryIsSigned(Integer id) {
        AccountManager accountManager = accountManagerRepository.getOne(id);
        Message message;
        Tx2504Req tx2504Request = new Tx2504Req();
        tx2504Request.setTxCode("2504");
        tx2504Request.setInstitutionID(chinaPayClearNetConfig.getClassicMemberId());
        tx2504Request.setAccountNumber(accountManager.getAccount());
        try {
            LOGGER.debug("中金支付查询是否签约请求:{}", JSONObject.toJSONString(tx2504Request));
            BaseService tx2504Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class,
                    chinaPayClearNetConfig.getQuickRealTimeSignStatusUrl());
            RequestResult requestResult = tx2504Service.doBusiness(tx2504Request);
            LOGGER.debug("中金支付查询是否签约返回:{}", JSONObject.toJSONString(requestResult));

            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                Tx2504Res tx2504Response = (Tx2504Res) requestResult.getResult().get(0);
                int status = tx2504Response.getStatus();
                //status为20未绑定,10已绑定
                if (status == 10) {
                    //已绑定,将返回的绑定流水号提取出来,保存到数据库中
                    accountManager.setChinaPayClearNetIsSigned(1);
                    accountManager.setChinaPayClearNetProtocolNo(tx2504Response.getTxSNBinding());
                    accountManagerRepository.saveAndFlush(accountManager);
                    message = Message.success("当前卡号已完成签约");
                } else {
                    //未绑定
                    message = Message.fail("当前卡号需签约");
                }
            } else {
                message = Message.fail("中金支付签约状态查询失败", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("中金签约状态查询异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("中金签约状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
        }
        return message;
    }

    /**
     * 发送短信验证码
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回包含短信发送结果的Message对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message sendSignSmsCode(AccountManager accountManager) {
        Message message;
        AccountManager accountManagerById = accountManagerRepository.getOne(accountManager.getId());
        Tx2531Req tx2531Request = new Tx2531Req();
        tx2531Request.setInstitutionID(chinaPayClearNetConfig.getClassicMemberId());
        String txSNBinding = null;
        try {
            txSNBinding = GUIDGenerator.genGUID();
        } catch (Exception e) {
            LOGGER.error("生成绑定流水号异常,记录id:{},账户名:{},账户{}", accountManager.getId(), accountManager.getAccountName(), accountManager.getAccount());
            e.printStackTrace();
        }
        if (StringUtils.isBlank(txSNBinding)) {
            message = Message.fail("生成绑定流水号失败");
            return message;
        }

        tx2531Request.setTxSNBinding(txSNBinding);
        tx2531Request.setBankID(ChinaPayClearNetBankCodeEnum.getCodeByBankName(accountManagerById.getBankName()));
        tx2531Request.setAccountName(accountManager.getAccountName());
        tx2531Request.setAccountNumber(accountManager.getAccount());
        tx2531Request.setIdentificationNumber(accountManager.getCertificateNo());
        tx2531Request.setIdentificationType("0");
        tx2531Request.setPhoneNumber(accountManager.getMobile());
        tx2531Request.setCardType("10");
        tx2531Request.setValidDate("");
        tx2531Request.setCvn2("");
        try {
            BaseService tx2531Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class,
                    chinaPayClearNetConfig.getQuickRealTimeSignSmsCodeUrl());
            RequestResult requestResult = tx2531Service.doBusiness(tx2531Request);
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //将绑卡流水号返回到前端页面
                message = Message.success();
                message.setData(txSNBinding);
            } else {
                message = Message.fail("中金签约短信发送失败");
            }
        } catch (Exception e) {
            LOGGER.error("发送中金签约短信异常,记录id:" + accountManager.getId() + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("中金签约短信发送异常", ConstantsEnum.REQUEST_FAIL.getCode());
        }
        return message;
    }

    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @return 返回签约结果的Message对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message sign(AccountManager accountManager) {
        Message message;

        Tx2532Req tx2532Request = new Tx2532Req();
        tx2532Request.setInstitutionID(chinaPayClearNetConfig.getClassicMemberId());
        tx2532Request.setTxSNBinding(accountManager.getMerOrderNo());
        tx2532Request.setSMSValidationCode(accountManager.getSmsCode());
        tx2532Request.setValidDate("");
        tx2532Request.setCvn2("");
        try {
            BaseService tx2532Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class,
                    chinaPayClearNetConfig.getQuickRealTimeSignUrl());
            RequestResult requestResult = tx2532Service.doBusiness(tx2532Request);
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                Tx2532Res tx2532Response = (Tx2532Res) requestResult.getResult().get(0);
                int status = tx2532Response.getStatus();
                //绑定状态:status 20=绑定失败 30=绑定成功
                if (status == 30) {
                    message = Message.success("中金签约成功");
                    AccountManager accountManagerById = accountManagerRepository.getOne(accountManager.getId());
                    accountManagerById.setChinaPayClearNetProtocolNo(accountManager.getMerOrderNo());
                    accountManagerById.setChinaPayClearNetIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManagerById);
                } else {
                    int verifyStatus = tx2532Response.getVerifyStatus();
                    //短信验证状态 20=验证码超时 30=验证未通过 40=验证通过
                    String msg = "中金签约失败";
                    if (verifyStatus == 20) {
                        msg = "短信验证码超时";
                    } else if (verifyStatus == 30) {
                        msg = "短信验证码错误";
                    }
                    message = Message.fail(msg);
                }
            } else {
                message = Message.fail("中金签约失败");
            }
        } catch (Exception e) {
            LOGGER.error("发送中金签约异常,记录id:" + accountManager.getId() + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("中金签约异常", ConstantsEnum.REQUEST_FAIL.getCode());
        }
        return message;
    }

    /**
     * 代扣
     *
     * @param mortgageDeductions mortgageDeduction列表
     * @throws Exception 执行代扣异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {

        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {
            Tx2511Req tx2511Request = beanConverter.transToTx2511Req(mortgageDeduction);
            mortgageDeduction.setOrdId(tx2511Request.getPaymentNo());
            mortgageDeduction.setMerId(tx2511Request.getInstitutionID());
            mortgageDeduction.setCuryId("156");
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_QUICK.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_QUICK.getCode());
            mortgageDeduction.setPayTime(new Date());

            try {
                LOGGER.debug("中金快捷支付请求:{}", JSONObject.toJSONString(tx2511Request));
                BaseService tx2511Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class,
                        chinaPayClearNetConfig.getQuickRealTimeUrl());
                RequestResult requestResult = tx2511Service.doBusiness(tx2511Request);
                LOGGER.debug("中金快捷支付返回:{}", JSONObject.toJSONString(requestResult));
                if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    Tx2511Res tx2511Response = (Tx2511Res) requestResult.getResult().get(0);
                    int status = tx2511Response.getStatus();
                    //交易状态 10=处理中 20=支付成功 30=支付失败
                    if (status == 20) {
                        mortgageDeduction.setIssuccess("1");
                        mortgageDeduction.setResult(tx2511Response.getCode());
                        mortgageDeduction.setErrorResult("交易成功");
                        calculateHandlingCharge(mortgageDeduction);
                    } else if (status == 30) {
                        //代扣失败
                        JSONObject jsonObject = XmlUtils.documentToJSONObject(tx2511Response.getResponsePlainText());
                        JSONObject body = (JSONObject) jsonObject.getJSONArray("Body").get(0);
                        mortgageDeduction.setErrorResult(body.getString("ResponseMessage"));
                        mortgageDeduction.setResult(body.getString("ResponseCode"));
                        mortgageDeduction.setIssuccess("0");
                    }
                }
            } catch (Exception e) {
                LOGGER.error("中金快捷支付异常,记录id"+mortgageDeduction.getId(),e);
            }
        }

    }

    /**
     * 查询代扣结果
     *
     * @param mortgageDeduction 代扣记录
     * @return 返回包含代扣查询结果Message对象
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message queryPayResult(MortgageDeduction mortgageDeduction) {
        Message message;
        // 创建交易请求对象
        Tx2512Req tx2512Request = new Tx2512Req();
        tx2512Request.setInstitutionID(chinaPayClearNetConfig.getClassicMemberId());
        tx2512Request.setPaymentNo(mortgageDeduction.getOrdId());
        try {
            BaseService tx2512Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class,
                    chinaPayClearNetConfig.getQuickRealTimeQueryResultUrl());
            RequestResult requestResult = tx2512Service.doBusiness(tx2512Request);
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                Tx2512Res tx2512Response = (Tx2512Res) requestResult.getResult().get(0);
                int status = tx2512Response.getStatus();
                //交易状态status:10=处理中 20=支付成功 30=支付失败
                if (status == 20) {
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(tx2512Response.getCode());
                    mortgageDeduction.setErrorResult("交易成功");
                    calculateHandlingCharge(mortgageDeduction);
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                } else if (status == 30) {
                    //代扣失败
                    JSONObject jsonObject = XmlUtils.documentToJSONObject(tx2512Response.getResponsePlainText());
                    JSONObject body = (JSONObject) jsonObject.getJSONArray("Body").get(0);
                    mortgageDeduction.setErrorResult(body.getString("ResponseMessage"));
                    mortgageDeduction.setResult(body.getString("ResponseCode"));
                    mortgageDeduction.setIssuccess("0");
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                }
                message = Message.success();
            } else {
                message = Message.fail("代扣状态查询失败");
            }
        } catch (Exception e) {
            LOGGER.error("发送中金快捷支付结果查询异常,记录id:" + mortgageDeduction.getId() + ",账户名:" + mortgageDeduction.getCustomerName(), e);
            message = Message.fail("代扣状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
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
        BigDecimal totalAmount = mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2());
        mortgageDeduction.setHandlingCharge(totalAmount.multiply(chinaPayClearNetConfig.getQuickRealTimeCharge()));
    }


}
