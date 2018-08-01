package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息查询条件
 * @date: Created in 2018/7/30 10:29
 * @Modified By:
 */
public class NonDeductionRepaymentInfoQueryCondition {
    /**
     * 还款的开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repaymentStartDate;
    /**
     * 还款的结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repaymentEndDate;

    /**
     * 客户名称
     */
    private String customerName;

    /**
     * 合同号
     */
    private String contractNo;

    /**
     * 导入开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date importStartDate;

    /**
     * 导入的结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date importEndDate;


    /**
     * 是否上传入账文件
     */
    private String isUploaded;


    /**
     * 是否有合同号
     */
    private String isIntegrated;


    public Date getRepaymentStartDate() {
        return repaymentStartDate;
    }

    public void setRepaymentStartDate(Date repaymentStartDate) {
        this.repaymentStartDate = repaymentStartDate;
    }

    public Date getRepaymentEndDate() {
        return repaymentEndDate;
    }

    public void setRepaymentEndDate(Date repaymentEndDate) {
        this.repaymentEndDate = repaymentEndDate;
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

    public Date getImportStartDate() {
        return importStartDate;
    }

    public void setImportStartDate(Date importStartDate) {
        this.importStartDate = importStartDate;
    }

    public Date getImportEndDate() {
        return importEndDate;
    }

    public void setImportEndDate(Date importEndDate) {
        this.importEndDate = importEndDate;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }



    public String getIsIntegrated() {
        return isIntegrated;
    }

    public void setIsIntegrated(String isIntegrated) {
        this.isIntegrated = isIntegrated;
    }
}
