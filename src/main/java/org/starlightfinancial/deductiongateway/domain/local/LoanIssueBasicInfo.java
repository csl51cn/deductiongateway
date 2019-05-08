package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放基本信息.引入@NamedEntityGraph是为了避免N+1查询问题.
 * @date: Created in 2018/9/17 16:26
 * @Modified By:
 */
@NamedEntityGraph(name = "LoanIssueBasicInfo.loanIssues", attributeNodes = {@NamedAttributeNode("loanIssues")})
@Entity(name = "BU_LOAN_BASIC_INFO")
public class LoanIssueBasicInfo {

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
     * 业务编号
     */
    @Column(name = "business_no")
    private String businessNo;

    /**
     * 收款人类型:1-个人,2-企业
     */
    @Column(name = "to_account_type")
    private String toAccountType;

    /**
     * 放款金额
     */
    @Column(name = "issue_amount")
    private BigDecimal issueAmount;


    /**
     * 收款人姓名
     */
    @Column(name = "to_account_name")
    private String toAccountName;


    /**
     * 收款人银行帐号
     */
    @Column(name = "to_acc_no")
    private String toAccountNo;

    /**
     * 收款人开户行名称
     */
    @Column(name = "to_bank_name_id")
    private Integer toBankNameId;


    /**
     * 收款人开户行省名
     */
    @Column(name = "to_bank_province")
    private String toBankProvince;


    /**
     * 收款人开户行市名
     */
    @Column(name = "to_bank_city")
    private String toBankCity;

    /**
     * 收款人开户行支行名,不包含省市名
     */
    @Column(name = "to_bank_branch")
    private String toBankBranch;


    /**
     * 证件号
     */
    @Column(name = "identity_no")
    private String identityNo;

    /**
     * 手机号
     */
    @Column(name = "mobile_no")
    private String mobileNo;

    /**
     * 交易渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 交易记录
     */
    @JsonManagedReference
    @OneToMany(targetEntity = LoanIssue.class, mappedBy = "loanIssueBasicInfo", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LoanIssue> loanIssues;

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

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getToAccountType() {
        return toAccountType;
    }

    public void setToAccountType(String toAccountType) {
        this.toAccountType = toAccountType;
    }

    public BigDecimal getIssueAmount() {
        return issueAmount;
    }

    public void setIssueAmount(BigDecimal issueAmount) {
        this.issueAmount = issueAmount;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public Integer getToBankNameId() {
        return toBankNameId;
    }

    public void setToBankNameId(Integer toBankNameId) {
        this.toBankNameId = toBankNameId;
    }

    public String getToBankProvince() {
        return toBankProvince;
    }

    public void setToBankProvince(String toBankProvince) {
        this.toBankProvince = toBankProvince;
    }

    public String getToBankCity() {
        return toBankCity;
    }

    public void setToBankCity(String toBankCity) {
        this.toBankCity = toBankCity;
    }

    public String getToBankBranch() {
        return toBankBranch;
    }

    public void setToBankBranch(String toBankBranch) {
        this.toBankBranch = toBankBranch;
    }

    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public List<LoanIssue> getLoanIssues() {
        return loanIssues;
    }

    public void setLoanIssues(List<LoanIssue> loanIssues) {
        this.loanIssues = loanIssues;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }


    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
