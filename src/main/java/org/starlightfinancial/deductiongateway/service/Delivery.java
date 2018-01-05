package org.starlightfinancial.deductiongateway.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.UnionPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.ErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

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
    UnionPayConfig unionPayConfig;

    @Autowired
    BaofuConfig baofuConfig;

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

    private void deliveryUnionPay(List<GoPayBean> list) {
        for (GoPayBean goPayBean : list) {
            MortgageDeduction mortgageDeduction = goPayBean.transToMortgageDeduction();
            mortgageDeduction.setPayTime(new Date());
            // 应对httpClientUtil返回抛异常的情况,将订单号保存,以保证我方数据库记录和银联的记录一致,方便排查错误
            try {
                Map map = httpClientUtil.send(unionPayConfig.getUrl(), goPayBean.aggregationToList());
                String returnData = (String) map.get("returnData");
                String payStat = returnData.substring(returnData.indexOf("PayStat") + 16, returnData.indexOf("PayStat") + 20);
                mortgageDeduction.setResult(payStat);
                if (StringUtils.equals(Constant.SUCCESS, payStat)) {
                    mortgageDeduction.setIssuccess("1");
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(payStat));
                result.add(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                result.add(mortgageDeduction);
            }
        }
    }

    private void deliveryBaoFu(List<RequestParams> list) {
        for (RequestParams requestParams : list) {
            MortgageDeduction mortgageDeduction = requestParams.switchToMortgageDeduction();
            mortgageDeduction.setPayTime(new Date());
            try {
                Map map = httpClientUtil.send(baofuConfig.getUrl(), requestParams.switchToNvpList());
                String returnData = (String) map.get("returnData");
                returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
                returnData = SecurityUtil.Base64Decode(returnData);
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                returnData = jsonObject.getObject("resp_msg", String.class);
                if (StringUtils.equals("交易成功", returnData)) {
                    mortgageDeduction.setIssuccess("1");
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
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
