package org.starlightfinancial.deductiongateway.baofu.domain;

import com.alibaba.fastjson.annotation.JSONField;
import org.apache.http.message.BasicNameValuePair;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 宝付代扣分账交易请求参数
 * Created by sili.chen on 2017/12/25
 */
public class RequestParams {
    private String version; // 版本号
    private String terminalId; // 终端号
    private String txnType; // 交易类型
    private String txnSubType; // 交易子类型
    private String memberId; // 商户号
    private String dataType; // 数据类型
    private String dataContent; // 加密数据串
    private DataContent content;
    private String contractNo; // 合同号
    private BigDecimal bxAmount; // 本息金额
    private BigDecimal fwfAmount; // 服务费金额

    public List<BasicNameValuePair> switchToNvpList() {
        List<BasicNameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("version", version));
        nvps.add(new BasicNameValuePair("terminal_id", terminalId));
        nvps.add(new BasicNameValuePair("txn_type", txnType));
        nvps.add(new BasicNameValuePair("txn_sub_type", txnSubType));
        nvps.add(new BasicNameValuePair("member_id", memberId));
        nvps.add(new BasicNameValuePair("data_type", dataType));
        nvps.add(new BasicNameValuePair("data_content", dataContent));
        return nvps;
    }

    public MortgageDeduction switchToMortgageDeduction() {
        MortgageDeduction mortgageDeduction = new MortgageDeduction();
        mortgageDeduction.setApplyMainId(-1);
        mortgageDeduction.setOrdId(content.getTransId());
        mortgageDeduction.setCustomerNo("");
        mortgageDeduction.setCustomerName(content.getIdHolder());
        mortgageDeduction.setContractNo(contractNo);
        mortgageDeduction.setParam1(BankCodeEnum.getIdByCode(content.getPayCode()));
        mortgageDeduction.setParam2("0");
        mortgageDeduction.setParam3(content.getAccNo());
        mortgageDeduction.setParam4(content.getIdHolder());
        mortgageDeduction.setParam5(content.getIdCardType());
        mortgageDeduction.setParam6(content.getIdCard());
        mortgageDeduction.setMerId(content.getMemberId());
        mortgageDeduction.setSplitData1(bxAmount);
        mortgageDeduction.setCuryId("156");
        mortgageDeduction.setOrderDesc("宝付");
        mortgageDeduction.setSplitType(txnType);
        mortgageDeduction.setSplitData2(fwfAmount);
        mortgageDeduction.setCreatId(113);
        mortgageDeduction.setCreateDate(new Date());
        mortgageDeduction.setIssuccess("");
        mortgageDeduction.setErrorResult("");
        mortgageDeduction.setIsoffs("0");
        mortgageDeduction.setType("0");
        mortgageDeduction.setTarget(content.getFeeMemberId());

        return mortgageDeduction;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }

    public DataContent getContent() {
        return content;
    }

    public void setContent(DataContent content) {
        this.content = content;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getBxAmount() {
        return bxAmount;
    }

    public void setBxAmount(BigDecimal bxAmount) {
        this.bxAmount = bxAmount;
    }

    public BigDecimal getFwfAmount() {
        return fwfAmount;
    }

    public void setFwfAmount(BigDecimal fwfAmount) {
        this.fwfAmount = fwfAmount;
    }
}
