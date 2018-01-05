package org.starlightfinancial.deductiongateway.baofu.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by sili.chen on 2017/12/25
 */
public class DataContent {
    @JSONField(name = "txn_sub_type")
    private String txnSubType; // 交易子类
    @JSONField(name = "biz_type")
    private String bizType; // 接入类型
    @JSONField(name = "terminal_id")
    private String terminalId; // 终端号
    @JSONField(name = "member_id")
    private String memberId; // 商户号
    @JSONField(name = "pay_code")
    private String payCode; // 银行编码
    @JSONField(name = "pay_cm")
    private String payCm; // 安全标识
    @JSONField(name = "acc_no")
    private String accNo; // 卡号
    @JSONField(name = "id_card_type")
    private String idCardType; // 身份证类型
    @JSONField(name = "id_card")
    private String idCard; // 身份证号
    @JSONField(name = "id_holder")
    private String idHolder; // 持卡人姓名
    @JSONField(name = "mobile")
    private String mobile; // 银行卡绑定手机号
    @JSONField(name = "valid_date")
    private String validDate; // 卡有效期
    @JSONField(name = "valid_no")
    private String validNo; // 卡安全码
    @JSONField(name = "trans_id")
    private String transId; // 商户订单号
    @JSONField(name = "txn_amt")
    private String txnAmt; // 交易金额
    @JSONField(name = "trade_date")
    private String tradeDate; // 订单日期
    @JSONField(name = "additional_info")
    private String additionalInfo; // 附加字段
    @JSONField(name = "req_reserved")
    private String reqReserved; // 请求放保留域
    @JSONField(name = "trans_serial_no")
    private String transSerialNo; // 商户流水号
    @JSONField(name = "share_info")
    private String shareInfo; // 分账信息
    @JSONField(name = "notify_url")
    private String notifyUrl; // 通知地址
    @JSONField(name = "fee_member_id")
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
