package org.starlightfinancial.deductiongateway.domain.local;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 代付交易结果导出Excel行元素对应实体类
 * @date: Created in 2018/10/30 16:38
 * @Modified By:
 */
public class LoanIssueBasicInfoExcelRow {


    /**
     * 业务编号
     */
    private String businessNo;

    /**
     * 合同编号
     */
    private String contractNo;


    /**
     * 收款人姓名
     */
    private String toAccountName;


    /**
     * 收款人银行帐号
     */
    private String toAccountNo;


    /**
     * 放款金额
     */
    private BigDecimal issueAmount;


    /**
     * 收款人开户行
     */
    private Integer toBankNameId;


    /**
     * 收款人开户行省名
     */
    private String toBankProvince;


    /**
     * 收款人开户行市名
     */
    private String toBankCity;

    /**
     * 收款人开户行支行名,不包含省市名
     */
    private String toBankBranch;

    /**
     * 证件号
     */
    private String identityNo;

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 交易渠道
     */
    private String channel;


    /**
     * 订单号
     */
    private String transactionNo;

    /**
     * 交易状态
     */
    private String transactionStatus;

    /**
     * 交易发起时间
     */
    private Date transactionStartTime;

    /**
     * 交易完成时间
     */
    private Date transactionEndTime;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getToAccountName() {
        return toAccountName;
    }

    public void setToAccountName(String toAccountName) {
        this.toAccountName = toAccountName;
    }

    public String getToAccountNo() {
        return toAccountNo;
    }

    public void setToAccountNo(String toAccountNo) {
        this.toAccountNo = toAccountNo;
    }

    public BigDecimal getIssueAmount() {
        return issueAmount;
    }

    public void setIssueAmount(BigDecimal issueAmount) {
        this.issueAmount = issueAmount;
    }

    public Integer getToBankNameId() {
        return toBankNameId;
    }

    public void setToBankNameId(Integer toBankNameId) {
        this.toBankNameId = toBankNameId;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Date getTransactionStartTime() {
        return transactionStartTime;
    }

    public void setTransactionStartTime(Date transactionStartTime) {
        this.transactionStartTime = transactionStartTime;
    }

    public Date getTransactionEndTime() {
        return transactionEndTime;
    }

    public void setTransactionEndTime(Date transactionEndTime) {
        this.transactionEndTime = transactionEndTime;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getToBankProvince() {
        return toBankProvince;
    }

    public void setToBankProvince(String toBankProvince) {
        this.toBankProvince = toBankProvince;
    }
}
