package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.ChinaPayCertTypeEnum;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 宝付协议支付
 * @date: Created in 2018/6/5 11:40
 * @Modified By:
 */
@Service("0002")
public class BaoFuProtocolStrategyImpl implements OperationStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaoFuProtocolStrategyImpl.class);

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private BaofuConfig baofuConfig;

    @Autowired
    private ChinaPayConfig chinaPayConfig;

    @Autowired
    private BeanConverter beanConverter;


    /**
     * 查询是否签约
     *
     * @param id 记录id
     * @return 返回包含查询结果的Message对象
     */
    @Override
    public Message queryIsSigned(Integer id) {
        AccountManager accountManager = accountManagerRepository.getOne(id);
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("accNo", accountManager.getAccount()));

        Message message;
        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignStatusUrl(), basicNameValuePairs);
        } catch (Exception e) {
            LOGGER.error("宝付签约状态查询异常,记录id:" + id + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("宝付签约状态查询异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }
        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //绑定成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                String protocols = result.getString("protocols");
                //签约协议号|用户ID|银行卡号|银行编码|银行名称
                String[] accInfo = protocols.split("\\|");
                accountManager.setBaofuProtocolNo(accInfo[0]);
                accountManager.setBaofuIsSigned(1);
                accountManagerRepository.saveAndFlush(accountManager);
                message = Message.success("当前卡号已完成签约");
            } else {
                if (StringUtils.equals(jsonObject.getString("error_code"), BFErrorCodeEnum.BF00134.getCode())) {
                    message = Message.fail("当前卡号需签约");
                } else {
                    message = Message.fail("宝付签约状态查询失败", ConstantsEnum.NO_DATA_RESPONSE.getCode());
                }
            }
        } else {
            message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
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

        Message message = null;
        //账户信息:银行卡号|持卡人姓名|证件号|手机号|银行卡安全码|银行卡有效期   银行卡安全码和银行卡有效期贷记卡才有,借记卡没有,留空
        StringBuilder accInfo = new StringBuilder();
        accInfo.append(accountManager.getAccount()).append("|").append(accountManager.getAccountName()).append("|")
                .append(accountManager.getCertificateNo()).append("|").append(accountManager.getMobile()).append("||");
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        //卡类型:101	借记卡，102 信用卡
        basicNameValuePairs.add(new BasicNameValuePair("cardType", "101"));
        //证件类型:01 身份证
        basicNameValuePairs.add(new BasicNameValuePair("idCardType", ChinaPayCertTypeEnum.getCodeByDesc(accountManager.getCertificateType())));
        basicNameValuePairs.add(new BasicNameValuePair("accInfo", accInfo.toString()));

        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignSmsCodeUrl(), basicNameValuePairs);
            if (map.containsKey("returnData")) {
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                    //发送短信成功
                    JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                    String bizRespCode = result.getString("biz_resp_code");
                    if (StringUtils.equals(bizRespCode, "0000")) {
                        //将商户订单号返回到前端页面
                        message = Message.success();
                        message.setData(result.getString("unique_code"));
                    } else {
                        message = Message.fail("宝付签约短信发送失败,失败原因:" + BFErrorCodeEnum.getValueByCode(bizRespCode));
                    }

                } else {
                    message = Message.fail("宝付签约短信发送失败,宝付返回原因:" + jsonObject.getString("reason"));
                }
            } else {
                message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
            }
        } catch (Exception e) {
            LOGGER.error("发送宝付签约短信异常,记录id:" + accountManager.getId() + ",账户名:" + accountManager.getAccountName() + ",账户:" + accountManager.getAccount(), e);
            message = Message.fail("宝付签约短信发送异常", ConstantsEnum.REQUEST_FAIL.getCode());
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
    @Override
    public Message sign(AccountManager accountManager) {
        Message message = null;
        AccountManager accountManagerById = accountManagerRepository.getOne(accountManager.getId());
        //格式：预签约唯一码|短信验证码
        String uniqueCode = accountManager.getMerOrderNo() + "|" + accountManager.getSmsCode();
        ArrayList<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("uniqueCode", uniqueCode));
        Map map = null;
        try {
            map = HttpClientUtil.send(baofuConfig.getProtocolSignUrl(), basicNameValuePairs);
        } catch (Exception e) {
            LOGGER.error("宝付签约异常,记录id:" + accountManagerById.getId() + ",账户名:" + accountManagerById.getAccountName() + ",账户:" + accountManagerById.getAccount(), e);
            message = Message.fail("宝付签约异常", ConstantsEnum.REQUEST_FAIL.getCode());
            return message;
        }

        if (map.containsKey("returnData")) {
            String returnData = (String) map.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(jsonObject.getString("error_code"), RsbCodeEnum.ERROR_CODE_01.getCode())) {
                //请求成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                String bizRespCode = result.getString("biz_resp_code");
                if (StringUtils.equals(bizRespCode, BFErrorCodeEnum.BF00000.getCode())) {
                    //签约成功
                    accountManagerById.setBaofuProtocolNo(result.getString("protocol_no"));
                    accountManagerById.setBaofuIsSigned(1);
                    accountManagerRepository.saveAndFlush(accountManagerById);
                    message = Message.success("宝付签约成功");
                } else {
                    //签约失败
                    message = Message.fail("宝付签约失败");
                }
            } else {
                message = Message.fail("宝付签约失败");
            }
        } else {
            message = Message.fail("未获得宝付返回信息", ConstantsEnum.NO_DATA_RESPONSE.getCode());
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
            BaoFuRequestParams baoFuRequestParams = beanConverter.transToBaoFuRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(baoFuRequestParams.getTransId());
            mortgageDeduction.setMerId(baofuConfig.getProtocolMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            //设置渠道信息
            mortgageDeduction.setChannel(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getCode());
            mortgageDeduction.setPayTime(Utility.convertToDate(baoFuRequestParams.getSendTime(), "yyyy-MM-dd HH:mm:ss"));

            try {
                Map map = HttpClientUtil.send(baofuConfig.getProtocolUrl(), baoFuRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult( StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
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

        Message message = null;
        List<BasicNameValuePair> list = new ArrayList<>();
        list.add(new BasicNameValuePair("origTransId", mortgageDeduction.getOrdId()));
        list.add(new BasicNameValuePair("origTradeDate", Utility.convertToString(mortgageDeduction.getPayTime(), "yyyy-MM-dd HH:mm:ss")));
        Map send = null;
        try {
            send = HttpClientUtil.send(baofuConfig.getProtocolQueryResultUrl(), list);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail("代扣状态查询失败");
        }
        if (send.containsKey("returnData")) {
            //有返回结果
            String returnData = (String) send.get("returnData");
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            if (StringUtils.equals(RsbCodeEnum.ERROR_CODE_01.getCode(), jsonObject.getString("error_code"))) {
                //查询成功
                JSONObject result = (JSONObject) jsonObject.getJSONArray("result").get(0);
                if (StringUtils.equals(BFErrorCodeEnum.BF00000.getCode(), result.getString("biz_resp_code"))) {
                    //支付成功
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(BFErrorCodeEnum.BF00000.getCode());
                    mortgageDeduction.setErrorResult("支付成功");
                    mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
                } else if (!StringUtils.equals(BFErrorCodeEnum.BF00113.getCode(), result.getString("biz_resp_code"))) {
                    //订单状态不为"BF00113,交易处理中，请稍后查询"为失败,状态为BF00113不处理
                    mortgageDeduction.setIssuccess("0");
                    mortgageDeduction.setResult(jsonObject.getString("error_code"));
                    mortgageDeduction.setErrorResult(BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code")));
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

    }
}
