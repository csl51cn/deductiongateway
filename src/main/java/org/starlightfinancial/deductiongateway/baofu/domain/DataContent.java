package org.starlightfinancial.deductiongateway.baofu.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author: Senlin.Deng
 * @Description: 宝付代扣DateContent
 * @date: Created in 2018/6/5 10:45
 * @Modified By:
 */
public class DataContent {

    /**
     * 交易子类
     */
    @JSONField(name = "txn_sub_type")
    private String txnSubType;

    /**
     * 接入类型
     */
    @JSONField(name = "biz_type")
    private String bizType;

    /**
     * 终端号
     */
    @JSONField(name = "terminal_id")
    private String terminalId;

    /**
     * 商户号
     */
    @JSONField(name = "member_id")
    private String memberId;

    /**
     * 银行编码
     */
    @JSONField(name = "pay_code")
    private String payCode;

    /**
     * 安全标识
     */
    @JSONField(name = "pay_cm")
    private String payCm;

    /**
     * 卡号
     */
    @JSONField(name = "acc_no")
    private String accNo;

    /**
     * 证件类型
     */
    @JSONField(name = "id_card_type")
    private String idCardType;

    /**
     * 证件号码
     */
    @JSONField(name = "id_card")
    private String idCard;

    /**
     * 持卡人姓名
     */
    @JSONField(name = "id_holder")
    private String idHolder;
    @JSONField(name = "mobile")

    /**
     * 银行卡绑定手机号
     */
    private String mobile;
    @JSONField(name = "valid_date")

    /**
     * 卡有效期
     */
    private String validDate;
    @JSONField(name = "valid_no")

    /**
     * 卡安全码
     */
    private String validNo;
    @JSONField(name = "trans_id")

    /**
     * 商户订单号
     */
    private String transId;

    /**
     * 交易金额
     */
    @JSONField(name = "txn_amt")
    private String txnAmt;

    /**
     * 订单日期
     */
    @JSONField(name = "trade_date")
    private String tradeDate;

    /**
     * 附加字段
     */
    @JSONField(name = "additional_info")
    private String additionalInfo;

    /**
     * 请求放保留域
     */
    @JSONField(name = "req_reserved")
    private String reqReserved;

    /**
     * 商户流水号
     */
    @JSONField(name = "trans_serial_no")
    private String transSerialNo;

    /**
     * 分账信息
     */
    @JSONField(name = "share_info")
    private String shareInfo;

    /**
     * 通知地址
     */
    @JSONField(name = "notify_url")
    private String notifyUrl;

    /**
     * 分账手续费商户
     */
    @JSONField(name = "fee_member_id")
    private String feeMemberId;

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
