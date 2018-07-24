package org.starlightfinancial.deductiongateway.domain.local;

import java.math.BigDecimal;

/**
 * @author: Senlin.Deng
 * @Description: 自动入账excel表格行元素对应实体类
 * @date: Created in 2018/7/24 14:23
 * @Modified By:
 */
public class AutoAccountingExcelRow {

    /**
     * 客户编号
     */
    private Integer customerId;

    /**
     * 客户姓名
     */
    private String customerName;


    /**
     * 合同编号
     */
    private String contractNo;


    /**
     * 还款日期,格式:yyyy/MM/dd
     */
    private String repaymentTermDate;

    /**
     * 入账日期,格式:yyyy/MM/dd
     */
    private String accountingDate;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 卡折编号
     */
    private String bankCardNo;

    /**
     * 本息
     */
    private BigDecimal principalAndInterest;

    /**
     * 服务费
     */
    private BigDecimal serviceFee;

    /**
     * 调查评估费
     */
    private BigDecimal evaluationFee;

    /**
     * 服务费入账公司
     */
    private String serviceFeeChargeCompany;


    /**
     * 调查评估费入账公司
     */
    private String evaluationFeeChargeCompany;

    /**
     * 持卡人姓名
     */
    private String bankCardOwner;

    /**
     * 证件号
     */
    private String certificateNo;


    /**
     * 扣款结果
     */
    private String status;

    /**
     * 原因
     */
    private String reason;


    /**
     * 是否代扣:是或者否
     */
    private String isDeduction;

    /**
     * 还款方式
     */
    private String repaymentMethod;


    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getRepaymentTermDate() {
        return repaymentTermDate;
    }

    public void setRepaymentTermDate(String repaymentTermDate) {
        this.repaymentTermDate = repaymentTermDate;
    }

    public String getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(String accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public BigDecimal getPrincipalAndInterest() {
        return principalAndInterest;
    }

    public void setPrincipalAndInterest(BigDecimal principalAndInterest) {
        this.principalAndInterest = principalAndInterest;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getEvaluationFee() {
        return evaluationFee;
    }

    public void setEvaluationFee(BigDecimal evaluationFee) {
        this.evaluationFee = evaluationFee;
    }

    public String getServiceFeeChargeCompany() {
        return serviceFeeChargeCompany;
    }

    public void setServiceFeeChargeCompany(String serviceFeeChargeCompany) {
        this.serviceFeeChargeCompany = serviceFeeChargeCompany;
    }

    public String getEvaluationFeeChargeCompany() {
        return evaluationFeeChargeCompany;
    }

    public void setEvaluationFeeChargeCompany(String evaluationFeeChargeCompany) {
        this.evaluationFeeChargeCompany = evaluationFeeChargeCompany;
    }

    public String getBankCardOwner() {
        return bankCardOwner;
    }

    public void setBankCardOwner(String bankCardOwner) {
        this.bankCardOwner = bankCardOwner;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIsDeduction() {
        return isDeduction;
    }

    public void setIsDeduction(String isDeduction) {
        this.isDeduction = isDeduction;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }
}
