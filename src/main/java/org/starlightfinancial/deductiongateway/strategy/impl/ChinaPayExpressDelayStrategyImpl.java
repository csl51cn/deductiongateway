package org.starlightfinancial.deductiongateway.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.config.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.ChinaPayDelayRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.RsbCodeEnum;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategy;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 银联新无卡代扣
 * @date: Created in 2018/6/5 11:37
 * @Modified By:
 */
@Service("0004")
public class ChinaPayExpressDelayStrategyImpl implements OperationStrategy {



    @Autowired
    BeanConverter beanConverter;

    @Autowired
    ChinaPayConfig chinaPayConfig;


    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

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
    @Override
    public void pay(List<MortgageDeduction> mortgageDeductions) throws Exception {
        for (MortgageDeduction mortgageDeduction : mortgageDeductions) {

            ChinaPayDelayRequestParams chinaPayDelayRequestParams = beanConverter.transToChinaPayDelayRequestParams(mortgageDeduction);
            chinaPayDelayRequestParams.setVersion("20140728");
            mortgageDeduction.setOrdId(chinaPayDelayRequestParams.getMerOrderNo());
            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_EXPRESS_DELAY.getCode());
            mortgageDeduction.setPayTime(new Date());
            try {
                Map map = HttpClientUtil.send(chinaPayConfig.getExpressDelayUrl(), chinaPayDelayRequestParams.transToNvpList());
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

    }



}
