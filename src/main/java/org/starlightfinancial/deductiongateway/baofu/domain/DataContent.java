package org.starlightfinancial.deductiongateway.baofu.domain;

/**
 * Created by sili.chen on 2017/12/25
 */
public class DataContent {
    private String txnSubType; // 交易子类
    private String bizType; // 接入类型
    private String terminalId; // 终端号
    private String memberId; // 商户号
    private String payCode; // 银行编码
    private String payCm; // 安全标识
    private String accNo; // 卡号
    private String idCardType; // 身份证类型
    private String idCard; // 身份证号
    private String idHolder; // 持卡人姓名
    private String mobile; // 银行卡绑定手机号
    private String validDate; // 卡有效期
    private String validNo; // 卡安全码
    private String transId; // 商户订单号
    private String txnAmt; // 交易金额
    private String tradeDate; // 订单日期
    private String additionalInfo; // 附加字段
    private String reqReserved; // 请求放保留域
    private String transSerialNo; // 商户流水号
    private String shareInfo; // 分账信息
    private String notifyUrl; // 通知地址
    private String feeMemberId; // 分账手续费商户

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getPayCode() {
        return payCode;
    }

    public void setPayCode(String payCode) {
        this.payCode = payCode;
    }

    public String getPayCm() {
        return payCm;
    }

    public void setPayCm(String payCm) {
        this.payCm = payCm;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getIdHolder() {
        return idHolder;
    }

    public void setIdHolder(String idHolder) {
        this.idHolder = idHolder;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getValidNo() {
        return validNo;
    }

    public void setValidNo(String validNo) {
        this.validNo = validNo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getTransSerialNo() {
        return transSerialNo;
    }

    public void setTransSerialNo(String transSerialNo) {
        this.transSerialNo = transSerialNo;
    }

    public String getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(String shareInfo) {
        this.shareInfo = shareInfo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getFeeMemberId() {
        return feeMemberId;
    }

    public void setFeeMemberId(String feeMemberId) {
        this.feeMemberId = feeMemberId;
    }
}