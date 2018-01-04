package org.starlightfinancial.deductiongateway.baofu.domain;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
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
}
