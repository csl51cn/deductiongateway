package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 代付交易记录
 * @date: Created in 2018/9/17 16:58
 * @Modified By:
 */
@Entity(name = "BU_LOAN_ISSUE")
public class LoanIssue {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    /**
     * 订单号
     */
    @Column(name = "transaction_no")
    private String transactionNo;

     /**
     * 宝付批次号
      *
     */
    @Column(name = "batch_id")
    private String batchId;

   /**
     * 交易状态
      *
     */
    @Column(name = "transaction_status")
    private String transactionStatus;

    /**
     * 交易摘要
      *
     */
    @Column(name = "transaction_summary")
    private String transactionSummary;
    
    
    /**
     * 交易备注
      *
     */
    @Column(name = "transaction_remark")
    private String transactionRemark;


    /**
     * 费用
     */
    @Column(name = "transaction_fee")
    private BigDecimal transactionFee;

    @Column(name = "accept_transaction_status")
    private String acceptTransactionStatus;

    /**
     * 交易发起时间
     */
    @Column(name = "transaction_start_time")
    private Date transactionStartTime;


    /**
     * 交易完成时间
     */
    @Column(name = "transaction_end_time")
    private Date transactionEndTime;

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


    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(String transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    public String getTransactionRemark() {
        return transactionRemark;
    }

    public void setTransactionRemark(String transactionRemark) {
        this.transactionRemark = transactionRemark;
    }

    public BigDecimal getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(BigDecimal transactionFee) {
        this.transactionFee = transactionFee;
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


    public String getAcceptTransactionStatus() {
        return acceptTransactionStatus;
    }

    public void setAcceptTransactionStatus(String acceptTransactionStatus) {
        this.acceptTransactionStatus = acceptTransactionStatus;
    }
}
