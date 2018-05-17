package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.UnionPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.*;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
    UnionPayConfig unionPayConfig;


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
            mortgageDeduction.setMerId(unionPayConfig.getMerId());
            mortgageDeduction.setCuryId(unionPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc("银联");
            mortgageDeduction.setPlanNo(0);
            //type为0表示已发起过代扣，type为1时未发起过代扣
            mortgageDeduction.setType("0");
            mortgageDeduction.setIsoffs("0");
            mortgageDeduction.setPayTime(new Date());

            try {
                Map map = httpClientUtil.send(unionPayConfig.getUrl(), unionPayRequestParams.transToNvpList());
                String returnData = (String) map.get("returnData");

                JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
                mortgageDeduction.setErrorResult(jsonObject.getString("reason"));
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
            RequestParams requestParams = new RequestParams();
            requestParams.setVersion(baofuConfig.getVersion());
            requestParams.setTerminalId(baofuConfig.getTerminalId());
            requestParams.setTxnType(baofuConfig.getTxnType());
            requestParams.setTxnSubType(baofuConfig.getTxnSubType());
            requestParams.setMemberId(baofuConfig.getMemberId());
            requestParams.setDataType(baofuConfig.getDataType());
            requestParams.setContractNo(mortgageDeduction.getContractNo());
            requestParams.setBxAmount(mortgageDeduction.getSplitData1());
            requestParams.setFwfAmount(mortgageDeduction.getRsplitData2());

            DataContent dataContent = mortgageDeduction.transToDataContent();

            // 有服务费时才分账
            if (mortgageDeduction.getSplitData2().doubleValue() > 0) {
                if (StringUtils.equals(unionPayConfig.getKaiyueServiceMemberId(), mortgageDeduction.getTarget())) {
                    dataContent.setShareInfo(baofuConfig.getMemberId() + "," + mortgageDeduction.getSplitData1().multiply(BigDecimal.valueOf(100)).setScale(0).toString()
                            + ";" + baofuConfig.getKaiyueServiceMemberId() + "," + mortgageDeduction.getSplitData2().multiply(BigDecimal.valueOf(100)).setScale(0).toString());
                } else if (StringUtils.equals(unionPayConfig.getRunkunServiceMemberId(), mortgageDeduction.getTarget())) {
                    dataContent.setShareInfo(baofuConfig.getMemberId() + "," + mortgageDeduction.getSplitData1().multiply(BigDecimal.valueOf(100)).setScale(0).toString()
                            + ";" + baofuConfig.getRunkunServiceMemberId() + "," + mortgageDeduction.getSplitData2().multiply(BigDecimal.valueOf(100)).setScale(0).toString());
                }
            } else {
                //无服务费的情况
                dataContent.setShareInfo(baofuConfig.getMemberId() + "," + dataContent.getTxnAmt());
            }

            // 分账手续费从本息账户扣除
            dataContent.setFeeMemberId(baofuConfig.getMemberId());

            dataContent.setTxnSubType(baofuConfig.getTxnSubType());
            dataContent.setBizType(baofuConfig.getBizType());
            dataContent.setTerminalId(baofuConfig.getTerminalId());
            dataContent.setMemberId(baofuConfig.getMemberId());
            dataContent.setPayCode(BankCodeEnum.getCodeById(mortgageDeduction.getParam1()));
            dataContent.setPayCm(baofuConfig.getPayCm());
            dataContent.setNotifyUrl(baofuConfig.getNotifyUrl());

            mortgageDeduction.setOrdId(dataContent.getTransId());
            mortgageDeduction.setMerId(dataContent.getMemberId());
            mortgageDeduction.setCuryId(unionPayConfig.getCuryId());
            mortgageDeduction.setOrderDesc("宝付");
            mortgageDeduction.setPlanNo(0);
            mortgageDeduction.setType("0");
            mortgageDeduction.setPayTime(new Date());


            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dataContent);
            String contentData = jsonObject.toString();
            System.out.println("contentData" + contentData);

            String base64str = SecurityUtil.Base64Encode(contentData);

            String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, baofuConfig.getPfxFile(), baofuConfig.getPriKey());
            requestParams.setDataContent(data_content);
            requestParams.setContent(dataContent);
            mortgageDeduction.setPayTime(new Date());
            try {
                Map map = httpClientUtil.send(baofuConfig.getUrl(), requestParams.switchToNvpList());
                String returnData = (String) map.get("returnData");
                returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
                returnData = SecurityUtil.Base64Decode(returnData);
                JSONObject parse = (JSONObject) JSONObject.parse(returnData);
                String resp_msg = parse.getObject("resp_msg", String.class);
                if (StringUtils.equals("交易成功", resp_msg)) {
                    mortgageDeduction.setIssuccess("1");
                } else {
                    mortgageDeduction.setIssuccess("0");
                }
                String resp_code = parse.getObject("resp_code", String.class);
                mortgageDeduction.setResult(resp_code);
                mortgageDeduction.setErrorResult(resp_msg);
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);//保存订单号
            }
        }
    }


}
