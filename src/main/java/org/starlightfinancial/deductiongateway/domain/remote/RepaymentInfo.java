package org.starlightfinancial.deductiongateway.domain.remote;


import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 还款信息:包含了代扣和非代扣还款
 * @date: Created in 2018/8/13 11:57
 * @Modified By:
 */
@Entity(name = "DATA_REPAYMENT_INFO")
public class RepaymentInfo {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 业务流水号
     */
    @Column(name = "date_id")
    private Long dateId;

    /**
     * 合同编号
     */
    @Column(name = "contract_no")
    private String contractNo;

    /**
     * 还款日期
     */
    @Column(name = "repayment_term_date")
    private Date repaymentTermDate;

    /**
     * 客户名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 还款方式
     */
    @Column(name = "repayment_method")
    private String repaymentMethod;

    /**
     * 还款金额
     */
    @Column(name = "repayment_amount")
    private BigDecimal repaymentAmount;

    /**
     * 入账银行
     */
    @Column(name = "bank_name")
    private String bankName;

    /**
     * 手续费
     */
    @Column(name = "handling_charge")
    private BigDecimal handlingCharge;


    /**
     * 入账公司
     */
    @Column(name = "charge_company")
    private String chargeCompany;

    /**
     * 还款类别
     */
    @Column(name = "repayment_type")
    private String repaymentType;

    /**
     * 是否是代扣
     */
    @Column(name = "is_deduction")
    private String isDeduction;

    /**
     * 在原始表中的id,也就是在代扣信息表和非代扣信息表中的原始id
     */
    @Column(name = "original_id")
    private Long originalId;

    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;


    /**
     * 创建人id
     */
    @Column(name = "create_id")
    private Integer createId;

    /**
     * 最后一个修改人id
     */
    @Column(name = "modified_id")
    private Integer modifiedId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public Date getRepaymentTermDate() {
        return repaymentTermDate;
    }

    public void setRepaymentTermDate(Date repaymentTermDate) {
        this.repaymentTermDate = repaymentTermDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }

    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }

    public String getIsDeduction() {
        return isDeduction;
    }

    public void setIsDeduction(String isDeduction) {
        this.isDeduction = isDeduction;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }

    public String getChargeCompany() {
        return chargeCompany;
    }

    public void setChargeCompany(String chargeCompany) {
        this.chargeCompany = chargeCompany;
    }

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }
}
