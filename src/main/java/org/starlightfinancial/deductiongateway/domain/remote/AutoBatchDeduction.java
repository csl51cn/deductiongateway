package org.starlightfinancial.deductiongateway.domain.remote;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sili.chen on 2017/8/25
 */
@Entity
public class AutoBatchDeduction {

    @Column(name = "dateid")
    private Integer dateId;

    @Column(name = "业务编号")
    private String busiNo;

    @Column(name = "还款账号银行")
    private String bankName;

    @Column(name = "代扣卡折类型")
    private String cardAndPassbook;

    @Column(name = "还款账户名")
    private String customerName;

    @Column(name = "还款账号")
    private String accout;

    @Column(name = "代扣人证件类型")
    private String certificateType;

    @Column(name = "代扣人证件号码")
    private String certificateNo;

    @Column(name = "bxplanid")
    private String bxplanid;

    @Column(name = "fwfplanid")
    private String fwfplanid;

    @Column(name = "计划期数")
    private String planVolume;

    @Column(name = "计划还款日")
    private Date planDate;

    @Column(name = "当期应还本息")
    private BigDecimal bxAmount;

    @Column(name = "本息未还总额")
    private BigDecimal bxBalance;

    @Column(name = "当期应还服务费")
    private BigDecimal fwfAmount;

    @Column(name = "服务费未还总额")
    private BigDecimal fwfBalance;

    @Column(name = "服务费管理司")
    private String fwfCompamny;

    @Column(name = "合同编号")
    private String contractNo;

    @Column(name = "本息预付款")
    private BigDecimal bxAdvance;

    @Column(name = "服务费预付款")
    private BigDecimal fwfAdvance;

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
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

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
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

    public String getBxplanid() {
        return bxplanid;
    }

    public void setBxplanid(String bxplanid) {
        this.bxplanid = bxplanid;
    }

    public String getFwfplanid() {
        return fwfplanid;
    }

    public void setFwfplanid(String fwfplanid) {
        this.fwfplanid = fwfplanid;
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

    public BigDecimal getBxBalance() {
        return bxBalance;
    }

    public void setBxBalance(BigDecimal bxBalance) {
        this.bxBalance = bxBalance;
    }

    public BigDecimal getFwfAmount() {
        return fwfAmount;
    }

    public void setFwfAmount(BigDecimal fwfAmount) {
        this.fwfAmount = fwfAmount;
    }

    public BigDecimal getFwfBalance() {
        return fwfBalance;
    }

    public void setFwfBalance(BigDecimal fwfBalance) {
        this.fwfBalance = fwfBalance;
    }

    public String getFwfCompamny() {
        return fwfCompamny;
    }

    public void setFwfCompamny(String fwfConpamny) {
        this.fwfCompamny = fwfConpamny;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public BigDecimal getBxAdvance() {
        return bxAdvance;
    }

    public void setBxAdvance(BigDecimal bxAdvance) {
        this.bxAdvance = bxAdvance;
    }

    public BigDecimal getFwfAdvance() {
        return fwfAdvance;
    }

    public void setFwfAdvance(BigDecimal fwfAdvance) {
        this.fwfAdvance = fwfAdvance;
    }

    @Override
    public String toString() {
        return "AutoBatchDeduction{" +
                "dateId=" + dateId +
                ", busiNo='" + busiNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardAndPassbook='" + cardAndPassbook + '\'' +
                ", customerName='" + customerName + '\'' +
                ", accout='" + accout + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateNo='" + certificateNo + '\'' +
                ", bxplanid='" + bxplanid + '\'' +
                ", fwfplanid='" + fwfplanid + '\'' +
                ", planVolume='" + planVolume + '\'' +
                ", planDate=" + planDate +
                ", bxAmount=" + bxAmount +
                ", bxBalance=" + bxBalance +
                ", fwfAmount=" + fwfAmount +
                ", fwfBalance=" + fwfBalance +
                ", fwfConpamny='" + fwfCompamny + '\'' +
                ", contractNo='" + contractNo + '\'' +
                ", bxAdvance=" + bxAdvance +
                ", fwfAdvance=" + fwfAdvance +
                '}';
    }
}
