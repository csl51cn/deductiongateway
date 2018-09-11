package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息
 * @date: Created in 2018/7/17 16:25
 * @Modified By:
 */
@Entity(name = "BU_NON_DEDUCTION_REPAYMENT_INFO")
public class NonDeductionRepaymentInfo implements Serializable {


    private static final long serialVersionUID = -8070620086463748397L;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repaymentTermDate;


    /**
     * 入账日期
     */
    @Column(name = "accounting_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date accountingDate;


    /**
     * 还款原始信息
     */
    @Column(name = "repayment_original_info")
    private String repaymentOriginalInfo;

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
     * 还款类别
     */
    @Column(name = "repayment_type")
    private String repaymentType;

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
     * 入账公司
     */
    @Column(name = "charge_company")
    private String chargeCompany;


    /**
     * 信息是否完整:判断标准是否有对应的合同号
     */
    @Column(name = "is_integrated")
    private String isIntegrated;

    @Column(name = "is_uploaded")
    private String isUploaded;
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


    /**
     * 手续费
     */
    @Column(name = "handling_charge")
    private BigDecimal handlingCharge;

    /**
     * 是否系统自动匹配出业务信息:0-否,1-是
     */
    @Column(name = "is_auto_matched")
    private String isAutoMatched;

    /**
     * 被拆分的原始ID,如果某条记录是拆分出来的,使用这个字段保存它的来源
     */
    @Column(name = "original_id")
    private Long originalId;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

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

    public String getRepaymentOriginalInfo() {
        return repaymentOriginalInfo;
    }

    public void setRepaymentOriginalInfo(String repaymentOriginalInfo) {
        this.repaymentOriginalInfo = repaymentOriginalInfo;
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

    public String getRepaymentType() {
        return repaymentType;
    }

    public void setRepaymentType(String repaymentType) {
        this.repaymentType = repaymentType;
    }

    public BigDecimal getRepaymentAmount() {
        return repaymentAmount;
    }

    public void setRepaymentAmount(BigDecimal repaymentAmount) {
        this.repaymentAmount = repaymentAmount;
    }

    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }

    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getChargeCompany() {
        return chargeCompany;
    }

    public void setChargeCompany(String chargeCompany) {
        this.chargeCompany = chargeCompany;
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

    public String getIsIntegrated() {
        return isIntegrated;
    }

    public void setIsIntegrated(String isIntegrated) {
        this.isIntegrated = isIntegrated;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }


    public Date getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(Date accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getIsAutoMatched() {
        return isAutoMatched;
    }

    public void setIsAutoMatched(String isAutoMatched) {
        this.isAutoMatched = isAutoMatched;
    }


    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(Long originalId) {
        this.originalId = originalId;
    }


    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NonDeductionRepaymentInfo that = (NonDeductionRepaymentInfo) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }


}
