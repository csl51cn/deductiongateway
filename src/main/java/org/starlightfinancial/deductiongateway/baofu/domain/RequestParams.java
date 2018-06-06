package org.starlightfinancial.deductiongateway.baofu.domain;

import org.apache.http.message.BasicNameValuePair;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 宝付代扣请求参数
 * @date: Created in 2018/6/5 10:46
 * @Modified By:
 */
public class RequestParams {
    /**
     * 版本号
     */
    private String version;

    /**
     * 终端号
     */
    private String terminalId;

    /**
     * 交易类型
     */
    private String txnType;

    /**
     * 交易子类型
     */
    private String txnSubType;

    /**
     * 商户号
     */
    private String memberId;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 加密数据串
     */
    private String dataContent;
    private DataContent content;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 本息金额
     */
    private BigDecimal bxAmount;

    /**
     * 服务费金额
     */
    private BigDecimal fwfAmount;

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
