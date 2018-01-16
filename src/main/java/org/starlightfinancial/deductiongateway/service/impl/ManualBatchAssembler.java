package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.UnionPayConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.ErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.UnionPayUtil;

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

    @Override
    public void assembleMessage() throws Exception {

    }

    public void saveUNIONPAY(List<MortgageDeduction> list) throws Exception {
        for (MortgageDeduction mortgageDeduction : list) {
            GoPayBean goPayBean = mortgageDeduction.transToGoPayBean();
            goPayBean.setMerId(unionPayConfig.getMerId());//商户号
            goPayBean.setCuryId(unionPayConfig.getCuryId());//订单交易币种
            goPayBean.setVersion(unionPayConfig.getVersion());//版本号
            goPayBean.setBgRetUrl(unionPayConfig.getBgRetUrl());//后台交易接收URL地址
            goPayBean.setPageRetUrl(unionPayConfig.getPageRetUrl());//页面交易接收URL地址
            goPayBean.setGateId(unionPayConfig.getGateId());//支付网关号
            goPayBean.setShareType(unionPayConfig.getType());//分账类型
            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer());
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                throw new Exception("银联报文签名异常");
            }
            goPayBean.setChkValue(chkValue);
            this.updateMortgageDeduction(mortgageDeduction, goPayBean);

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
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);//保存订单号
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
            if (mortgageDeduction.getSplitData2().doubleValue() > 0 ) {
                if ( StringUtils.equals("00145112", mortgageDeduction.getTarget())){
                    dataContent.setShareInfo(baofuConfig.getMemberId() + "," + mortgageDeduction.getSplitData1().multiply(BigDecimal.valueOf(100)).setScale(0).toString()
                            + ";" + baofuConfig.getKaiyueServiceMemberId() + "," + mortgageDeduction.getSplitData2().multiply(BigDecimal.valueOf(100)).setScale(0).toString());
                }else if (StringUtils.equals("00160808", mortgageDeduction.getTarget())){
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
            mortgageDeduction.setCuryId("156");
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
                mortgageDeduction.setErrorResult(BFErrorCodeEnum.getValueByCode(resp_code));
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);
            } catch (Exception e) {
                e.printStackTrace();
                mortgageDeductionRepository.saveAndFlush(mortgageDeduction);//保存订单号
            }
        }
    }


    private void updateMortgageDeduction(MortgageDeduction mortgageDeduction, GoPayBean goPayBean) {
        mortgageDeduction.setOrdId(goPayBean.getOrdId());
        mortgageDeduction.setMerId(goPayBean.getMerId());
        mortgageDeduction.setCuryId(goPayBean.getCuryId());
        mortgageDeduction.setOrderDesc(goPayBean.getOrdDesc());
        mortgageDeduction.setPlanNo(0);
        mortgageDeduction.setType("0");
        mortgageDeduction.setPayTime(new Date());
        mortgageDeduction.setOrderDesc("银联");
    }


}
