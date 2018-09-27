package org.starlightfinancial.deductiongateway.baofu.domain.payment.response;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Senlin.Deng
 * @Description: 代付交易查证返回信息
 * @date: Created in 2018/9/12 9:25
 * @Modified By:
 */
public class TransRespBF0040002 {

    /**
     * 宝付订单号
     */
    @JsonProperty(value = "trans_orderid")
    private String transOrderId;

    /**
     * 宝付批次号
     */
    @JsonProperty(value = "trans_batchid")
    private String transBatchId;

    /**
     * 商户订单号
     */
    @JsonProperty(value = "trans_no")
    private String transNo;

    /**
     * 转账金额
     */
    @JsonProperty(value = "trans_money")
    private String transMoney;

    /**
     * 收款人姓名
     */
    @JsonProperty(value = "to_acc_name")
    private String toAccName;

    /**
     * 收款人银行帐号
     */
    @JsonProperty(value = "to_acc_no")
    private String toAccNo;

    /**
     * 收款人开户行机构名
     */
    @JsonProperty(value = "to_acc_dept")
    private String toAccDept;

    /**
     * 交易手续费
     */
    @JsonProperty(value = "trans_fee")
    private String transFee;

    /**
     * 订单交易处理状态:
     * 0：转账中；
     * 1：转账成功；
     * -1：转账失败；
     * 2：转账退款
     */
    @JsonProperty(value = "state")
    private String state;

    /**
     * 备注(错误信息)
     */
    @JsonProperty(value = "trans_remark")
    private String transRemark;

    /**
     * 交易申请时间,格式：YYYY-MM-DD HH:mm:ss
     */
    @JsonProperty(value = "trans_starttime")
    private String transStartTime;

    /**
     * 交易完成时间,格式：YYYY-MM-DD HH:mm:ss
     */
    @JsonProperty(value = "trans_endtime")
    private String transEndTime;

    /**
     * 摘要
     */
    @JsonProperty(value = "trans_summary")
    private String transSummary;


    /**
     * 用途
     */
    @JsonProperty(value = "trans_reserved")
    private String transReserved;

    /**
     * 收款方宝付会员号 （宝付账户交易返回）
     */
    @JsonProperty(value = "to_member_id")
    private String toMemberId;

    public String getTransOrderId() {
        return transOrderId;
    }

    public void setTransOrderId(String transOrderId) {
        this.transOrderId = transOrderId;
    }

    public String getTransBatchId() {
        return transBatchId;
    }

    public void setTransBatchId(String transBatchId) {
        this.transBatchId = transBatchId;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    public String getTransMoney() {
        return transMoney;
    }

    public void setTransMoney(String transMoney) {
        this.transMoney = transMoney;
    }

    public String getToAccName() {
        return toAccName;
    }

    public void setToAccName(String toAccName) {
        this.toAccName = toAccName;
    }

    public String getToAccNo() {
        return toAccNo;
    }

    public void setToAccNo(String toAccNo) {
        this.toAccNo = toAccNo;
    }

    public String getToAccDept() {
        return toAccDept;
    }

    public void setToAccDept(String toAccDept) {
        this.toAccDept = toAccDept;
    }

    public String getTransFee() {
        return transFee;
    }

    public void setTransFee(String transFee) {
        this.transFee = transFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTransRemark() {
        return transRemark;
    }

    public void setTransRemark(String transRemark) {
        this.transRemark = transRemark;
    }

    public String getTransStartTime() {
        return transStartTime;
    }

    public void setTransStartTime(String transStartTime) {
        this.transStartTime = transStartTime;
    }

    public String getTransEndTime() {
        return transEndTime;
    }

    public void setTransEndTime(String transEndTime) {
        this.transEndTime = transEndTime;
    }

    public String getTransSummary() {
        return transSummary;
    }

    public void setTransSummary(String transSummary) {
        this.transSummary = transSummary;
    }

    public String getTransReserved() {
        return transReserved;
    }

    public void setTransReserved(String transReserved) {
        this.transReserved = transReserved;
    }

    public String getToMemberId() {
        return toMemberId;
    }

    public void setToMemberId(String toMemberId) {
        this.toMemberId = toMemberId;
    }

    @Override
    public String toString() {
        return "TransRespBF0040002{" +
                "transOrderId='" + transOrderId + '\'' +
                ", transBatchId='" + transBatchId + '\'' +
                ", transNo='" + transNo + '\'' +
                ", transMoney='" + transMoney + '\'' +
                ", toAccName='" + toAccName + '\'' +
                ", toAccNo='" + toAccNo + '\'' +
                ", toAccDept='" + toAccDept + '\'' +
                ", transFee='" + transFee + '\'' +
                ", state='" + state + '\'' +
                ", transRemark='" + transRemark + '\'' +
                ", transStartTime='" + transStartTime + '\'' +
                ", transEndTime='" + transEndTime + '\'' +
                ", transSummary='" + transSummary + '\'' +
                ", transReserved='" + transReserved + '\'' +
                ", toMemberId='" + toMemberId + '\'' +
                '}';
    }
}
