package org.starlightfinancial.deductiongateway.baofu.domain.payment.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * @author: senlin.deng
 * @Description: 代付交易拆分接口交易参数
 * @date: Created in 2018/9/10 15:20
 * @Modified By:
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonRootName(value = "trans_reqData")
public class TransReqBF0040004 {

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
     * 收款人银行名称
     */
    @JsonProperty(value = "to_bank_name")
    private String toBankName;

    /**
     * 收款人开户行省名
     */
    @JsonProperty(value = "to_pro_name")
    private String toProName;
    /**
     * 收款人开户行市名
     */
    @JsonProperty(value = "to_city_name")
    private String toCityName;

    /**
     * 收款人开户行机构名
     */
    @JsonProperty(value = "to_acc_dept")
    private String toAccDept;

    /**
     * 身份证号码
     */
    @JsonProperty(value = "trans_card_id")
    private String transCardId;

    /**
     * 手机号
     */
    @JsonProperty(value = "trans_mobile")
    private String transMobile;

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

    public String getToBankName() {
        return toBankName;
    }

    public void setToBankName(String toBankName) {
        this.toBankName = toBankName;
    }

    public String getToProName() {
        return toProName;
    }

    public void setToProName(String toProName) {
        this.toProName = toProName;
    }

    public String getToCityName() {
        return toCityName;
    }

    public void setToCityName(String toCityName) {
        this.toCityName = toCityName;
    }

    public String getToAccDept() {
        return toAccDept;
    }

    public void setToAccDept(String toAccDept) {
        this.toAccDept = toAccDept;
    }

    public String getTransCardId() {
        return transCardId;
    }

    public void setTransCardId(String transCardId) {
        this.transCardId = transCardId;
    }

    public String getTransMobile() {
        return transMobile;
    }

    public void setTransMobile(String transMobile) {
        this.transMobile = transMobile;
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

    @Override
    public String toString() {
        return "TransReqBF0040004{" +
                "transBatchId='" + transBatchId + '\'' +
                ", transNo='" + transNo + '\'' +
                ", transMoney='" + transMoney + '\'' +
                ", toAccName='" + toAccName + '\'' +
                ", toAccNo='" + toAccNo + '\'' +
                ", toBankName='" + toBankName + '\'' +
                ", toProName='" + toProName + '\'' +
                ", toCityName='" + toCityName + '\'' +
                ", toAccDept='" + toAccDept + '\'' +
                ", transCardId='" + transCardId + '\'' +
                ", transMobile='" + transMobile + '\'' +
                ", transSummary='" + transSummary + '\'' +
                ", transReserved='" + transReserved + '\'' +
                '}';
    }
}
