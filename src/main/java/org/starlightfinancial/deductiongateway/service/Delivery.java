package org.starlightfinancial.deductiongateway.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.ChinaPayDelayRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sili.chen on 2018/1/3
 */
@Component
public class Delivery extends Decorator {

    @Value("${batch.route.use}")
    private String router;

    @Autowired
    HttpClientUtil httpClientUtil;

    @Autowired
    ChinaPayConfig chinaPayConfig;

    @Autowired
    BaofuConfig baofuConfig;


    @Autowired
    BeanConverter beanConverter;

    List<MortgageDeduction> result;

    @Override
    public void doRoute() throws Exception {
        result = new ArrayList<>();
        super.doRoute();
        delivery();
    }

    private void delivery() {
        List list = ((Assembler) this.route).getResult();
        if ("UNIONPAY".equals(router)) {
            deliveryUnionPay(list);
        } else if ("BAOFU".equals(router)) {
            deliveryBaoFu(list);
        }

    }

    private void deliveryUnionPay(List<MortgageDeduction> list) {
        for (MortgageDeduction mortgageDeduction : list) {

            ChinaPayDelayRequestParams chinaPayDelayRequestParams = beanConverter.transToChinaPayDelayRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(chinaPayDelayRequestParams.getMerOrderNo());
            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(new Date());
            mortgageDeduction.setSplitType(chinaPayDelayRequestParams.getSplitType());
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_EXPRESS_REALTIME.getCode());

            // 应对httpClientUtil返回抛异常的情况,将订单号保存,以保证我方数据库记录和银联的记录一致,方便排查错误
            try {
                Map map = httpClientUtil.send(chinaPayConfig.getExpressDelayUrl(), chinaPayDelayRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                //返回0014表示数据接收成功,如果不为0014可以交易设置为失败
                if (!StringUtils.equals(jsonObject.getString("error_code"), "0014")) {
                    mortgageDeduction.setIssuccess("0");
                }
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                result.add(mortgageDeduction);
            }
        }
    }

    private void deliveryBaoFu(List<MortgageDeduction> list)  {
        for (MortgageDeduction mortgageDeduction : list) {
            BaoFuRequestParams baoFuRequestParams = beanConverter.transToBaoFuRequestParams(mortgageDeduction);

            mortgageDeduction.setMerId(baofuConfig.getProtocolMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(Utility.convertToDate(baoFuRequestParams.getSendTime(),"yyyy-MM-dd HH:mm:ss"));
            mortgageDeduction.setSplitType("");
            mortgageDeduction.setChannel(DeductionChannelEnum.BAO_FU_PROTOCOL_PAY.getCode());
            try {
                Map map = httpClientUtil.send(baofuConfig.getProtocolUrl(), baoFuRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                String errorCodeDesc = BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code"));
                mortgageDeduction.setErrorResult(StringUtils.isEmpty(errorCodeDesc) ? jsonObject.getString("reason") : errorCodeDesc);
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                result.add(mortgageDeduction);
            }
        }
    }

    public List<MortgageDeduction> getResult() {
        return result;
    }
}
