package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BaoFuRequestParams;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.domain.local.UnionPayRequestParams;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class ManualBatchAssembler extends Assembler {

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    HttpClientUtil httpClientUtil;

    @Autowired
    ChinaPayConfig chinaPayConfig;


    @Autowired
    BaofuConfig baofuConfig;

    @Autowired
    BeanConverter beanConverter;

    @Override
    public void assembleMessage() throws Exception {

    }

    public void saveUNIONPAY(List<MortgageDeduction> list) throws Exception {
        for (MortgageDeduction mortgageDeduction : list) {

            UnionPayRequestParams unionPayRequestParams = beanConverter.transToUnionPayRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(unionPayRequestParams.getMerOrderNo());
            mortgageDeduction.setMerId(chinaPayConfig.getExpressRealTimeMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.CHINA_PAY_QUICK_PAY.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.CHINA_PAY_QUICK_PAY.getCode());
            mortgageDeduction.setPayTime(new Date());

            try {
                Map map = httpClientUtil.send(chinaPayConfig.getExpressRealTimeUrl(), unionPayRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");

                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                mortgageDeduction.setErrorResult(ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("error_code")));
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                //返回0014表示数据接收成功,如果不为0014可以交易设置为失败
                if (!StringUtils.equals(jsonObject.getString("error_code"), "0014")) {
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            }
        }
    }

    public void saveBAOFU(List<MortgageDeduction> list) throws UnsupportedEncodingException {
        for (MortgageDeduction mortgageDeduction : list) {
            BaoFuRequestParams baoFuRequestParams = beanConverter.transToBaoFuRequestParams(mortgageDeduction);
            mortgageDeduction.setOrdId(baoFuRequestParams.getTransId());
            mortgageDeduction.setMerId(baofuConfig.getProtocolMemberId());
            mortgageDeduction.setCuryId(chinaPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc(DeductionChannelEnum.BAO_FU_AGREEMENT_PAY.getOrderDesc());
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            mortgageDeduction.setChannel(DeductionChannelEnum.BAO_FU_AGREEMENT_PAY.getCode());
            mortgageDeduction.setPayTime(Utility.convertToDate(baoFuRequestParams.getSendTime(),"yyyy-MM-dd HH:mm:ss"));

            try {
                Map map = httpClientUtil.send(baofuConfig.getProtocolUrl(), baoFuRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");
                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                mortgageDeduction.setErrorResult(BFErrorCodeEnum.getValueByCode(jsonObject.getString("error_code")));
                mortgageDeduction.setResult(jsonObject.getString("error_code"));
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                //保存订单号
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            }
        }
    }


}
