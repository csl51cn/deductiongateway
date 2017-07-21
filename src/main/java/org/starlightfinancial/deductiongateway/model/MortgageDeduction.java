package org.starlightfinancial.deductiongateway.model;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 记录贷扣的情况的数据
 *
 * @author DELL
 */
@Entity
@Table(name = "CUS_MORTGAGEDEUCTION")
public class MortgageDeduction implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "apid")
    private int applyMainId;

    @Column(name = "OrdId")
    private String ordId;

    @Column(name = "customerNo")
    private String customerNo;

    @Column(name = "customerName")
    private String customerName;

    @Column(name = "contractNo")
    private String contractNo;

    @Column(name = "Param1")
    private String param1;

    @Column(name = "Param2")
    private String param2;

    @Column(name = "Param3")
    private String param3;

    @Column(name = "Param4")
    private String param4;

    @Column(name = "Param5")
    private String param5;

    @Column(name = "Param6")
    private String param6;

    @Column(name = "MerId")
    private String merId;

    @Column(name = "splitData1")
    private BigDecimal splitData1;

    @Column(name = "CuryId")
    private String curyId;

    @Column(name = "orderDesc")
    private String orderDesc;

    @Column(name = "splitType")
    private String splitType;

    @Column(name = "splitData2")
    private BigDecimal splitData2;


    @Column(name = "creat_id")
    private int creatId;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "issuccess")
    private String issuccess;

    @Column(name = "errorResult")
    private String errorResult;

    @Column(name = "isoffs")
    private String isoffs;

    @Column(name = "type")
    private String type;//type为0时表示存放扣款统计的结果，type为1时表示存放扣款信息的维护

    @Column(name = "target")
    private String target;

    @Column(name = "result")
    private String result;

    @Column(name = "rsplitData1")
    private BigDecimal rsplitData1;

    @Column(name = "rsplitData2")
    private BigDecimal rsplitData2;

    @Column(name = "plan_No")
    private int planNo;

    @Transient
    String PlitData1;
    @Transient
    String PlitData2;

    public MortgageDeduction() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getApplyMainId() {
        return applyMainId;
    }

    public void setApplyMainId(int applyMainId) {
        this.applyMainId = applyMainId;
    }


    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
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


    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getParam4() {
        return param4;
    }

    public void setParam4(String param4) {
        this.param4 = param4;
    }

    public String getParam5() {
        return param5;
    }

    public void setParam5(String param5) {
        this.param5 = param5;
    }

    public String getParam6() {
        return param6;
    }

    public void setParam6(String param6) {
        this.param6 = param6;
    }

    public String getCuryId() {
        return curyId;
    }

    public void setCuryId(String curyId) {
        this.curyId = curyId;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }


    public BigDecimal getSplitData1() {
        return splitData1;
    }

    public void setSplitData1(BigDecimal splitData1) {
        this.splitData1 = splitData1;
    }

    public BigDecimal getSplitData2() {
        return splitData2;
    }

    public void setSplitData2(BigDecimal splitData2) {
        this.splitData2 = splitData2;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
    }

    public int getCreatId() {
        return creatId;
    }

    public void setCreatId(int creatId) {
        this.creatId = creatId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIssuccess() {
        return issuccess;
    }

    public void setIssuccess(String issuccess) {
        this.issuccess = issuccess;
    }

    public String getErrorResult() {
        return errorResult;
    }

    public void setErrorResult(String errorResult) {
        this.errorResult = errorResult;
    }

    public String getIsoffs() {
        return isoffs;
    }

    public void setIsoffs(String isoffs) {
        this.isoffs = isoffs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public BigDecimal getRsplitData1() {
        return rsplitData1;
    }

    public void setRsplitData1(BigDecimal rsplitData1) {
        this.rsplitData1 = rsplitData1;
    }

    public BigDecimal getRsplitData2() {
        return rsplitData2;
    }

    public void setRsplitData2(BigDecimal rsplitData2) {
        this.rsplitData2 = rsplitData2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getPlanNo() {
        return planNo;
    }

    public void setPlanNo(int planNo) {
        this.planNo = planNo;
    }

    public String getPlitData1() {
        return PlitData1;
    }

    public void setPlitData1(String plitData1) {
        PlitData1 = plitData1;
    }

    public String getPlitData2() {
        return PlitData2;
    }

    public void setPlitData2(String plitData2) {
        PlitData2 = plitData2;
    }


}
