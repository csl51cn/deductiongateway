package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.baofu.domain.RequestParams;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.SysDict;
import org.starlightfinancial.deductiongateway.domain.local.SysDictRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.DictionaryType;
import org.starlightfinancial.deductiongateway.utility.UnionPayUtil;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AutoBatchAssembler extends Assembler {
    @Autowired
    SysDictRepository sysDictRepository;

    @Value("${batch.route.use}")
    private String router;

    @Autowired
    private BaofuConfig baofuConfig;

    @Override
    public void assembleMessage() throws Exception {
        List<AutoBatchDeduction> list = ((Filter) this.route).getDeductionList();
        if ("UNIONPAY".equals(router)) {
            assembleUNIONPAY(list);
        } else if ("BAOFU".equals(router)) {
            assembleBAOFU(list);
        }
    }

    private void assembleUNIONPAY(List<AutoBatchDeduction> list) throws Exception {
        for (AutoBatchDeduction autoBatchDeduction : list) {
            GoPayBean goPayBean = autoBatchDeduction.transToGoPayBean();
            goPayBean.setParam1(handleBankName(goPayBean.getParam1()));
            goPayBean.setParam5(handleCertificateType(goPayBean.getParam5()));
            goPayBean.setParam6(goPayBean.getParam6().toUpperCase());//将身份证号中的X转换为大写
            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer());
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                throw new Exception("银联报文签名异常");
            }
            goPayBean.setChkValue(chkValue);
            getResult().add(goPayBean);
        }
    }

    private void assembleBAOFU(List<AutoBatchDeduction> list) throws UnsupportedEncodingException {
        for (AutoBatchDeduction autoBatchDeduction : list) {
            RequestParams requestParams = new RequestParams();
            requestParams.setVersion(baofuConfig.getVersion());
            requestParams.setTerminalId(baofuConfig.getTerminalId());
            requestParams.setTxnType(baofuConfig.getTxnType());
            requestParams.setTxnSubType(baofuConfig.getTxnSubType());
            requestParams.setMemberId(baofuConfig.getMemberId());
            requestParams.setDataType(baofuConfig.getDataType());
            requestParams.setContractNo(autoBatchDeduction.getContractNo());
            requestParams.setBxAmount(autoBatchDeduction.getBxAmount());
            requestParams.setFwfAmount(autoBatchDeduction.getFwfAmount());

            DataContent dataContent = autoBatchDeduction.transToDataContent();
            dataContent.setTxnSubType(baofuConfig.getTxnSubType());
            dataContent.setBizType(baofuConfig.getBizType());
            dataContent.setTerminalId(baofuConfig.getTerminalId());
            dataContent.setMemberId(baofuConfig.getMemberId());
            dataContent.setPayCode(BankCodeEnum.getCodeByBankName(autoBatchDeduction.getBankName()));
            dataContent.setPayCm(baofuConfig.getPayCm());
            dataContent.setNotifyUrl(baofuConfig.getNotifyUrl());

            JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dataContent);
            String contentData = jsonObject.toString();
            System.out.println("contentData" + contentData);

            String base64str = SecurityUtil.Base64Encode(contentData);
            String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, baofuConfig.getPfxFile(), baofuConfig.getPriKey());
            requestParams.setDataContent(data_content);
            requestParams.setContent(dataContent);
            getResult().add(requestParams);
        }
    }

    private String handleBankName(String bankName) {
        List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
        for (SysDict sysDict : openBankList) {
            if (StringUtils.equals(bankName, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }

    private String handleCertificateType(String certificateType) {
        List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
        for (SysDict sysDict : cTypeLst) {
            if (StringUtils.equals(certificateType, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }

        return "";
    }
}
