package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.enums.*;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 银联快捷支付
 * @date: Created in 2018/6/5 11:20
 * @Modified By:
 */
@Service("0001")
public class ChinaPayExpressRealTimeStrategyImpl implements OperationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChinaPayExpressRealTimeStrategyImpl.class);

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BeanConverter beanConverter;


    @Autowired
    private ChinaPayConfig chinaPayConfig;


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
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", accountManager.getAccount()));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(accountManager.getCertificateType())));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", accountManager.getCertificateNo()));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountManager.getAccountName()));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", accountManager.getMobile()));
        Message message;
        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealtimeSignStatusUrl(), basicNameValuePairs);
        } catch (Exception e) {
            LOGGER.error("银联签约状态查询异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("银联签约状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }
        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);

                //获取签约状态:00-未签约,01-已签约,02-签约失败,03-已撤销
                String signState = result.getString("SignState");
                if (StringUtils.equals("01", signState)) {
                    //已签约,将返回的协议号提取出来,保存到数据库中
                    JSONObject cardTranData = result.getJSONObject("CardTranData");
                    accountManager.setUnionpayProtocolNo(cardTranData.getString("ProtocolNo"));
                    accountManager.setUnionpayIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManager);
                    message = Message.success("当前卡号已完成签约");
                } else {
                    //除去已签约的其他状态
                    message = Message.fail("当前卡号需签约");
                }
            } else {
                message = Message.fail("银联签约状态查询失败", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } else {
            message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
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
        Message message = null;
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", accountManager.getAccount()));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(accountManager.getCertificateType())));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", accountManager.getCertificateNo()));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountManager.getAccountName()));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", accountManager.getMobile()));
        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeSignSmsCodeUrl(), basicNameValuePairs);
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    //发送短信成功
                    JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                    //将商户订单号返回到前端页面
                    message = Message.success();
                    message.setData(result.getString("MerOrderNo"));
                } else {
                    message = Message.fail("银联签约短信发送失败");
                }
            } else {
                message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("发送银联签约短信异常,记录id:" + accountManager.getId() + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("银联签约短信发送异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
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
        Message message = null;
        AccountManager accountManagerById = accountManagerRepository.getOne(accountManager.getId());
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", accountManager.getAccount()));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", ChinaPayCertTypeEnum.getCodeByDesc(accountManager.getCertificateType())));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", accountManager.getCertificateNo()));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", accountManager.getAccountName()));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", accountManager.getMobile()));
        basicNameValuePairs.add(new BasicNameValuePair("MerOrderNo", accountManager.getMerOrderNo()));
        basicNameValuePairs.add(new BasicNameValuePair("MobileAuthCode", accountManager.getSmsCode()));
        Map map = null;
        try {
            map = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeSignUrl(), basicNameValuePairs);
        } catch (Exception e) {
            LOGGER.error("银联签约异常,记录id:" + accountManager.getId() + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("银联签约异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }

        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                //获取签约状态:00-未签约,01-已签约,02-签约失败,03-已撤销
                String signState = result.getString("SignState");
                if (StringUtils.equals("01", signState)) {
                    //签约成功,将返回的协议号提取出来,保存到数据库中
                    JSONObject cardTranData = result.getJSONObject("CardTranData");
                    accountManagerById.setUnionpayProtocolNo(cardTranData.getString("ProtocolNo"));
                    accountManagerById.setUnionpayIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManagerById);
                    message = Message.success("银联签约成功");
                } else {
                    //除去签约成功的其他状态
                    message = Message.fail("银联签约失败");
                }
            } else {
                message = Message.fail("银联签约失败");
            }
        } else {
            message = Message.fail("未获得银联返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
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

            ChinaPayRealTimeRequestParams chinaPayRealTimeRequestParams = beanConverter.transToChinaPayRealTimeRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(chinaPayRealTimeRequestParams.getMerOrderNo());
            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode());
            mortgageDeduction.setPayTime(new Date());
            try {
                Map map = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeUrl(), chinaPayRealTimeRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");

                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                //返回0014表示数据接收成功,如果不为0014可以交易设置为失败
                if (!StringUtils.equals(jsonObject.getString("error_code"), "0014")) {
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Message queryPayResult(MortgageDeduction mortgageDeduction) {
        Message message = null;
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("MerOrderNo", mortgageDeduction.getOrdId()));
        list.add(new BasicNameValuePair("TranDate", Utility.convertToString(mortgageDeduction.getPayTime(), "yyyyMMdd")));
        Map send = null;
        try {
            send = HttpClientUtil.send(chinaPayConfig.getExpressRealTimeQueryResultUrl(), list);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("代扣状态查询失败");
        }
        //有返回结果
        if (send.containsKey("returnData")) {
            String returnData = (String) send.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(RsbCodeEnum.ERROR_CODE_01.getCode(), jsonObject.getString("error_code"))) {
                //查询成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                if (StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_001.getCode(), result.getString("OrderStatus"))) {
                    //支付成功
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_001.getCode());
                    mortgageDeduction.setErrorResult("支付成功");
                    calculateHandlingCharge(mortgageDeduction);
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                } else if (!StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_008.getCode(), result.getString("OrderStatus"))) {
                    //订单状态不为"0014,数据接收成功"为失败
                    mortgageDeduction.setIssuccess("0");
                    mortgageDeduction.setResult(result.getString("OrderStatus"));
                    mortgageDeduction.setErrorResult(ChinaPayReturnCodeEnum.getValueByCode(result.getString("OrderStatus")));
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                }
                message = Message.success();

            } else {
                //查询失败
                message = Message.fail("代扣状态查询失败");
            }
        } else {
            //无返回结果
            message = Message.fail("代扣状态查询失败");
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
        BigDecimal charge = totalAmount.multiply(chinaPayConfig.getExpressRealtimeCharge());
        BigDecimal min = BigDecimal.valueOf(0.08);
        if (charge.compareTo(min) < 0) {
            //手续费按照费率计算小于8分钱时,需要设置为8分钱
            mortgageDeduction.setHandlingCharge(min);
        } else {
            mortgageDeduction.setHandlingCharge(charge);
        }
    }

}
