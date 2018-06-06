
package org.starlightfinancial.deductiongateway.domain.remote;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by sili.chen on 2017/8/25
 */
@Entity
public class AutoBatchDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "dateid")
    private Integer dateId;

    @Column(name = "业务编号")
    private String busiNo;

    @Column(name = "还款账号银行")
    @NotEmpty(message = "还款账号银行不能为空")
    private String bankName;

    @Column(name = "代扣卡折类型")
    @NotEmpty(message = "代扣卡折类型不能为空")
    private String cardAndPassbook;

    @Column(name = "还款账户名")
    @NotEmpty(message = "还款账户名不能为空")
    private String customerName;

    @Column(name = "还款账号")
    @NotEmpty(message = "还款账号不能为空")
    private String accout;

    @Column(name = "代扣人证件类型")
    @NotEmpty(message = "代扣人证件类型不能为空")
    private String certificateType;

    @Column(name = "代扣人证件号码")
    @NotEmpty(message = "代扣人证件号码不能为空")
    private String certificateNo;

    @Column(name = "计划期数")
    private String planVolume;

    @Column(name = "计划还款日")
    private Date planDate;

    @Column(name = "当期应还本息")
    @DecimalMin(value = "0", message = "当期应还本息不能小于0")
    private BigDecimal bxAmount;

    @Column(name = "当期应还服务费")
    @DecimalMin(value = "0", message = "当期应还服务费不能小于0")
    private BigDecimal fwfAmount;

    @Column(name = "服务费管理司")
    @NotEmpty(message = "服务费管理司不能为空")
    private String fwfCompamny;

    @Column(name = "合同编号")
    @NotEmpty(message = "合同编号不能为空")
    private String contractNo;


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

    @Override
    public String toString() {
        return "AutoBatchDeduction{" +
                "id=" + id +
                ", dateId=" + dateId +
                ", busiNo='" + busiNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardAndPassbook='" + cardAndPassbook + '\'' +
                ", customerName='" + customerName + '\'' +
                ", accout='" + accout + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateNo='" + certificateNo + '\'' +
                ", planVolume='" + planVolume + '\'' +
                ", planDate=" + planDate +
                ", bxAmount=" + bxAmount +
                ", fwfAmount=" + fwfAmount +
                ", fwfCompamny='" + fwfCompamny + '\'' +
                ", contractNo='" + contractNo + '\'' +
                '}';
    }
}
