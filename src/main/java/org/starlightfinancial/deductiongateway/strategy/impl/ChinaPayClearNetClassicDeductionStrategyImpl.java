package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.config.ChinaPayClearNetConfig;
import org.starlightfinancial.deductiongateway.config.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HessianProxyFactoryUtils;
import org.starlightfinancial.deductiongateway.utility.XmlUtils;
import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.Tx2011Req;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.Tx2020Req;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2011Res;
import org.starlightfinancial.rpc.hessian.entity.cpcn.response.Tx2020Res;
import org.starlightfinancial.rpc.hessian.service.cpcn.BaseService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付单笔代扣渠道
 * @date: Created in 2019/4/16 14:54
 * @Modified By:
 */
@Service("0006")
public class ChinaPayClearNetClassicDeductionStrategyImpl implements OperationStrategy {

    @Autowired
    private BeanConverter beanConverter;

    @Autowired
    private ChinaPayConfig chinaPayConfig;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private ChinaPayClearNetConfig chinaPayClearNetConfig;

    private static final Logger LOGGER = LoggerFactory.getLogger(ChinaPayClearNetClassicDeductionStrategyImpl.class);

    /**
     * 计算手续费时使用的界限值
     */
    private static final BigDecimal TWENTY_THOUSAND = new BigDecimal(20000);


    /**
     * 查询是否签约
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer id) {
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
     * @throws Exception 执行代扣异常
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {
        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {

            Tx2011Req tx2011Req = beanConverter.transToTx2011Req(mortgageDeduction);
            mortgageDeduction.setOrdId(tx2011Req.getTxSN());
            mortgageDeduction.setMerId(tx2011Req.getInstitutionID());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getCode());
            mortgageDeduction.setPayTime(new Date());
            try {
                LOGGER.debug("中金支付单笔代扣请求:{}", JSONObject.toJSONString(tx2011Req));
                BaseService tx2011Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class, chinaPayClearNetConfig.getClassicDeductionUrl());
                RequestResult requestResult = tx2011Service.doBusiness(tx2011Req);
                LOGGER.debug("中金支付单笔代扣返回:{}", JSONObject.toJSONString(requestResult));
                if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    Tx2011Res tx2011Response = (Tx2011Res) requestResult.getResult().get(0);
                    int status = tx2011Response.getStatus();
                    if(status == 20){
                        //status为20是正在处理,需要设置状态
                        mortgageDeduction.setErrorResult("正在处理中,稍后查询结果");
                        mortgageDeduction.setResult(tx2011Response.getCode());
                        mortgageDeduction.setIssuccess("3");
                        calculateHandlingCharge(mortgageDeduction);
                    }else  if (status == 30) {
                        //代扣成功
                        mortgageDeduction.setErrorResult("交易成功");
                        mortgageDeduction.setResult(tx2011Response.getCode());
                        mortgageDeduction.setIssuccess("1");
                        calculateHandlingCharge(mortgageDeduction);
                    } else if (status == 40) {
                        //代扣失败
                        JSONObject jsonObject = XmlUtils.documentToJSONObject(tx2011Response.getResponsePlainText());
                        JSONObject body = (JSONObject) jsonObject.getJSONArray("Body").get(0);
                        mortgageDeduction.setErrorResult(body.getString("ResponseMessage"));
                        mortgageDeduction.setResult(body.getString("ResponseCode"));
                        mortgageDeduction.setIssuccess("0");
                    }
                } else {
                    mortgageDeduction.setErrorResult(requestResult.getReason());
                    mortgageDeduction.setResult(requestResult.getErrorCode());
                    mortgageDeduction.setIssuccess("0");
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        Message message;
        Tx2020Req tx2020Req = new Tx2020Req();
        tx2020Req.setInstitutionID(chinaPayClearNetConfig.getClassicMemberId());
        tx2020Req.setTxSN(mortgageDeduction.getOrdId());

        try {
            BaseService tx2020Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class, chinaPayClearNetConfig.getClassicQueryResultUrl());
            RequestResult requestResult = tx2020Service.doBusiness(tx2020Req);
            if (StringUtils.equals(requestResult.getErrorCode(), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                Tx2020Res tx2020Response = (Tx2020Res) requestResult.getResult().get(0);
                int status = tx2020Response.getStatus();
                if(status == 20){
                    //status为20是正在处理,需要设置状态
                    mortgageDeduction.setErrorResult("正在处理中,稍后查询结果");
                    mortgageDeduction.setResult(tx2020Response.getCode());
                    mortgageDeduction.setIssuccess("3");
                    calculateHandlingCharge(mortgageDeduction);
                }else if (status == 30) {
                    //代扣成功
                    mortgageDeduction.setErrorResult("交易成功");
                    mortgageDeduction.setResult(tx2020Response.getCode());
                    mortgageDeduction.setIssuccess("1");
                    //设置手续费
                    calculateHandlingCharge(mortgageDeduction);
                } else if (status == 40) {
                    //代扣失败
                    JSONObject jsonObject = XmlUtils.documentToJSONObject(tx2020Response.getResponsePlainText());
                    JSONObject body = (JSONObject) jsonObject.getJSONArray("Body").get(0);
                    mortgageDeduction.setErrorResult(body.getString("ResponseMessage"));
                    mortgageDeduction.setResult(body.getString("ResponseCode"));
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                message = Message.success();
            } else {
                message = Message.fail("查询失败");
            }
        } catch (Exception e) {
            LOGGER.error("中金支付单笔代扣查询失败", e);
            message = Message.fail("查询失败");
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
        //中金单笔代扣每笔按金额*费率收手续费费率,并且手续费有最低值,如果低于最低值按照最低值收取费用
        //中国银行与其余银行费率不同
        String bank = mortgageDeduction.getParam1();
        BigDecimal charge;
        if (StringUtils.equals(bank, BankCodeEnum.BANK_CODE_04.getId())) {
            //中国银行
            charge = totalAmount.multiply(chinaPayClearNetConfig.getBocCharge());
        } else {
            //其余银行
            charge = totalAmount.multiply(chinaPayClearNetConfig.getClassicCharge());
        }
        if (charge.compareTo(chinaPayClearNetConfig.getClassicLowestCharge()) < 0) {
            charge = chinaPayClearNetConfig.getClassicLowestCharge();
        }
        mortgageDeduction.setHandlingCharge(charge);
    }

}

