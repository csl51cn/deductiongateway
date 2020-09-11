package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * 记录贷扣的情况的数据
 *
 * @author sili.chen
 */
@Entity(name = "BU_MORTGAGEDEUCTION")
public class MortgageDeduction implements Serializable, Cloneable {


    private static final long serialVersionUID = -8390682495500009884L;
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

    @Column(name = "paytime")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    /**
     * 支付状态:0:失败,1:成功,其他展示为暂无结果
     */
    @Column(name = "issuccess")
    private String issuccess;

    @Column(name = "errorresult")
    private String errorResult;

    @Column(name = "isoffs")
    private String isoffs;

    /**
     * 最初建表时使用的规则:type为0表示已发起过代扣，type为1时未发起过代扣
     */
    @Column(name = "type")
    private String type;

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

    @Column(name = "checkstate")
    private String checkState;

    /**
     * 分账状态:0 失败, 1成功
     */
    @Column(name = "ledgerstate")
    private String ledgerState;


    /**
     * 扣款渠道
     */
    @Column(name = "channel")
    private String channel;


    /**
     * 是否上传自动入账文件:0-否,1-是
     */
    @Column(name = "is_uploaded")
    private String isUploaded;

    /**
     * 手续费
     */
    @Column(name = "handling_charge")
    private BigDecimal handlingCharge;

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

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
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

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public String getLedgerState() {
        return ledgerState;
    }

    public void setLedgerState(String ledgerState) {
        this.ledgerState = ledgerState;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }

    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public MortgageDeduction cloneSelf() {
        MortgageDeduction o = null;
        try {
            o = (MortgageDeduction) clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
