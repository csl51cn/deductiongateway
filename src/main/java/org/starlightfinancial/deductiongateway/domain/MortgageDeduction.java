package org.starlightfinancial.deductiongateway.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 记录贷扣的情况的数据
 *
 * @author sili.chen
 */
@Entity(name = "BU_MORTGAGEDEUCTION_TEST")
public class MortgageDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "apid")
    private int applyMainId;

    @Column(name = "ordid")
    private String ordId;

    @Column(name = "customerno")
    private String customerNo;

    @Column(name = "customername")
    private String customerName;

    @Column(name = "contractno")
    private String contractNo;

    @Column(name = "param1")
    private String param1;

    @Column(name = "param2")
    private String param2;

    @Column(name = "param3")
    private String param3;

    @Column(name = "param4")
    private String param4;

    @Column(name = "param5")
    private String param5;

    @Column(name = "param6")
    private String param6;

    @Column(name = "merid")
    private String merId;

    @Column(name = "splitdata1")
    private BigDecimal splitData1;

    @Column(name = "curyid")
    private String curyId;

    @Column(name = "orderdesc")
    private String orderDesc;

    @Column(name = "splittype")
    private String splitType;

    @Column(name = "splitdata2")
    private BigDecimal splitData2;

    @Column(name = "creatid")
    private int creatId;

    @Column(name = "createdate")
    private Date createDate;

    @Column(name = "issuccess")
    private String issuccess;

    @Column(name = "errorresult")
    private String errorResult;

    @Column(name = "isoffs")
    private String isoffs;

    @Column(name = "type")
    private String type;//type为0时表示存放扣款统计的结果，type为1时表示存放扣款信息的维护

    @Column(name = "target")
    private String target;

    @Column(name = "result")
    private String result;

    @Column(name = "rsplitdata1")
    private BigDecimal rsplitData1;

    @Column(name = "rsplitdata2")
    private BigDecimal rsplitData2;

    @Column(name = "planno")
    private int planNo;

    public MortgageDeduction() {
    }

    public MortgageDeduction(int applyMainId) {
        this.applyMainId = applyMainId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getApplyMainId() {
        return applyMainId;
    }

    public void setApplyMainId(int applyMainId) {
        this.applyMainId = applyMainId;
    }

    public String getOrdId() {
        return ordId;
    }

    public void setOrdId(String ordId) {
        this.ordId = ordId;
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

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public BigDecimal getSplitData1() {
        return splitData1;
    }

    public void setSplitData1(BigDecimal splitData1) {
        this.splitData1 = splitData1;
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

    public BigDecimal getSplitData2() {
        return splitData2;
    }

    public void setSplitData2(BigDecimal splitData2) {
        this.splitData2 = splitData2;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    public int getPlanNo() {
        return planNo;
    }

    public void setPlanNo(int planNo) {
        this.planNo = planNo;
    }
}
