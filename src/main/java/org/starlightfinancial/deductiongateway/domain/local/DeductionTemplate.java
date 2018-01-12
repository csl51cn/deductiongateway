
package org.starlightfinancial.deductiongateway.domain.local;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 代扣模板
 */
@Entity(name="BU_DEDUCTION_TEMPLATE")
public class DeductionTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "dateid")
    private Integer dateId;

    @Column(name = "业务编号")
    private String bizNo;

    @Column(name = "合同编号")
    private String contractNo;

    @Column(name = "还款账号银行")
    private String bankName;

    @Column(name = "代扣卡折类型")
    private String cardAndPassbook;

    @Column(name = "还款账户名")
    private String customerName;

    @Column(name = "还款账号")
    private String account;

    @Column(name = "代扣人证件类型")
    private String certificateType;

    @Column(name = "代扣人证件号码")
    private String certificateNo;

    @Column(name = "计划期数")
    private String planVolume;

    @Column(name = "计划还款日")
    private Date planDate;

    @Column(name = "当期应还本息")
    private BigDecimal bxAmount;

    @Column(name = "当期应还服务费")
    private BigDecimal fwfAmount;

    @Column(name = "服务费管理司")
    private String fwfCompamny;


    @Column(name = "是否代扣成功")
    private String isSuccess;

    @Column(name = "当期未扣本息")
    private BigDecimal bxRemain;

    @Column(name = "当期未扣服务费")
    private BigDecimal fwfRemain;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getBizNo() {
        return bizNo;
    }

    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardAndPassbook() {
        return cardAndPassbook;
    }

    public void setCardAndPassbook(String cardAndPassbook) {
        this.cardAndPassbook = cardAndPassbook;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getPlanVolume() {
        return planVolume;
    }

    public void setPlanVolume(String planVolume) {
        this.planVolume = planVolume;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
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

    public String getFwfCompamny() {
        return fwfCompamny;
    }

    public void setFwfCompamny(String fwfCompamny) {
        this.fwfCompamny = fwfCompamny;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }

    public BigDecimal getBxRemain() {
        return bxRemain;
    }

    public void setBxRemain(BigDecimal bxRemain) {
        this.bxRemain = bxRemain;
    }

    public BigDecimal getFwfRemain() {
        return fwfRemain;
    }

    public void setFwfRemain(BigDecimal fwfRemain) {
        this.fwfRemain = fwfRemain;
    }
}
