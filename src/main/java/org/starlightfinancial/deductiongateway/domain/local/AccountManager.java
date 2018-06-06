package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 卡号管理实体类
 *
 * @author senlin.deng
 */
@Entity(name = "BU_ACCOUNT_MANAGER")
public class AccountManager {


    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;


    @Column(name = "dateid")
    private Integer dateId;

    /**
     * 合同编号
     */
    @Column(name = "contractno")
    private String contractNo;

    /**
     * 业务编号
     */
    @Column(name = "bizno")
    private String bizNo;

    /**
     * 开户行
     */
    @Column(name = "bankname")
    private String bankName;

    /**
     * 账户名
     */
    @Column(name = "accountname")
    private String accountName;

    /**
     * 账户卡号
     */
    @Column(name = "account")
    private String account;

    /**
     * 证件类型
     */
    @Column(name = "certificatetype")
    private String certificateType;

    /**
     * 证件号码
     */
    @Column(name = "certificateno")
    private String certificateNo;


    /**
     * 扣款顺序
     */
    @Column(name = "sort")
    private Integer sort;

    /**
     * 修改时间
     */
    @Column(name = "changetime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date changeTime;

    /**
     * 放款时间
     */
    @Column(name = "loandate")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date loanDate;

    /**
     * 标志是否开启自动代扣,0:关闭,1:开启
     */
    @Column(name = "isenabled")
    private Integer isEnabled = 1;


    /**
     * 手机号
     */
    @Column(name = "mobile")
    private String mobile;

    /**
     * 银联是否签约,0:未签约,1:已签约
     */
    @Column(name = "unionpay_is_signed")
    private Integer unionpayIsSigned = 0;

    /**
     * 宝付是否签约,0:未签约,1:已签约
     */
    @Column(name = "baofu_is_signed")
    private Integer baofuIsSigned = 0;

    /**
     * 银联协议号
     */
    @Column(name = "unionpay_protocol_no")
    private String unionpayProtocolNo;


    /**
     * 宝付协议号
     */
    @Column(name = "baofu_protocol_no")
    private String baofuProtocolNo;

    /**
     * 客户id
     */
    @Column(name = "customerid")
    private String customerId;


    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Column(name = "createtime")
    private Date createTime;

    /**
     * 签约短信验证码
     */
    @Transient
    private String smsCode;
    /**
     * 短信验证码对应的订单号
     */
    @Transient
    private String merOrderNo;

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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Integer getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Integer isEnabled) {
        this.isEnabled = isEnabled;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUnionpayProtocolNo() {
        return unionpayProtocolNo;
    }

    public void setUnionpayProtocolNo(String unionpayProtocolNo) {
        this.unionpayProtocolNo = unionpayProtocolNo;
    }

    public String getBaofuProtocolNo() {
        return baofuProtocolNo;
    }

    public void setBaofuProtocolNo(String baofuProtocolNo) {
        this.baofuProtocolNo = baofuProtocolNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Integer getUnionpayIsSigned() {
        return unionpayIsSigned;
    }

    public void setUnionpayIsSigned(Integer unionpayIsSigned) {
        this.unionpayIsSigned = unionpayIsSigned;
    }

    public Integer getBaofuIsSigned() {
        return baofuIsSigned;
    }

    public void setBaofuIsSigned(Integer baofuIsSigned) {
        this.baofuIsSigned = baofuIsSigned;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    @Override
    public String toString() {
        return "AccountManager{" +
                "id=" + id +
                ", dateId=" + dateId +
                ", contractNo='" + contractNo + '\'' +
                ", bizNo='" + bizNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountName='" + accountName + '\'' +
                ", account='" + account + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateNo='" + certificateNo + '\'' +
                ", sort=" + sort +
                ", changeTime=" + changeTime +
                ", loanDate=" + loanDate +
                ", isEnabled=" + isEnabled +
                ", mobile='" + mobile + '\'' +
                ", unionpayIsSigned=" + unionpayIsSigned +
                ", baofuIsSigned=" + baofuIsSigned +
                ", unionpayProtocolNo='" + unionpayProtocolNo + '\'' +
                ", baofuProtocolNo='" + baofuProtocolNo + '\'' +
                ", customerId='" + customerId + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
