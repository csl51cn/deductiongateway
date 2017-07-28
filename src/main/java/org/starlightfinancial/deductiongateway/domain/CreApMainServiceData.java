package org.starlightfinancial.deductiongateway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class CreApMainServiceData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private int id; //主键，也是申请的id

    @Column(name = "customer_id")
    private int customerId; //借款信息表的数据的ＩＤ，客户的id

    @Column(name = "Service_fee_rates")
    private BigDecimal serviceFeeRates; //服务费的申请利率

    @Column(name = "fService_fee_rates")
    private BigDecimal fServiceFeeRates; //服务费的审批利率

    @Column(name = "Org_manager_id")
    private int orgManagerId; //该服务费由哪个公司管理

    @Column(name = "Create_id")
    private int createId;

    @Column(name = "Create_date")
    private Date createDate;

    @Column(name = "contract_no")
    private String contractNo; //合同编号

    @Column(name = "contractId")
    private int contractId; //合同ID

    @Column(name = "customer_unique_no")
    private String customerUniqueNo;

    @Column(name = "customerName")
    private String customerName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getServiceFeeRates() {
        return serviceFeeRates;
    }

    public void setServiceFeeRates(BigDecimal serviceFeeRates) {
        this.serviceFeeRates = serviceFeeRates;
    }

    public BigDecimal getfServiceFeeRates() {
        return fServiceFeeRates;
    }

    public void setfServiceFeeRates(BigDecimal fServiceFeeRates) {
        this.fServiceFeeRates = fServiceFeeRates;
    }

    public int getOrgManagerId() {
        return orgManagerId;
    }

    public void setOrgManagerId(int orgManagerId) {
        this.orgManagerId = orgManagerId;
    }

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCustomerUniqueNo() {
        return customerUniqueNo;
    }

    public void setCustomerUniqueNo(String customerUniqueNo) {
        this.customerUniqueNo = customerUniqueNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getContractId() {
        return contractId;
    }

    public void setContractId(int contractId) {
        this.contractId = contractId;
    }

}
