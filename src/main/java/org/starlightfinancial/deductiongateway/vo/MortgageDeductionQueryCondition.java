package org.starlightfinancial.deductiongateway.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 查询参数
 * @date: Created in 2020/12/23 10:51
 * @Modified By:
 */
public class MortgageDeductionQueryCondition {

    /**
     * 开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startDate;
    /**
     * 结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endDate;

    /**
     * 页码
     */
    private  Integer pageNumber;

    /**
     * 每页显示的记录数
     */
    private  Integer  pageSize;

    /**
     * 客户姓名
     */
    private String customerName;



    /**
     * 页面类型,0:已执行代扣的数据 1:未执行
     */
    private String type;

    /**
     * 合同号
     */
    private String contractNo;

    /**
     * 日期类型:0-创建日期,1-代扣日期
     */
    private String dateType;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getDateType() {
        return dateType;
    }

    public void setDateType(String dateType) {
        this.dateType = dateType;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
