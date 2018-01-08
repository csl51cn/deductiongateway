package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.utility.MerSeq;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * 记录贷扣的情况的数据
 *
 * @author sili.chen
 */
@Entity(name = "BU_MORTGAGEDEUCTION")
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

    @Column(name = "paytime")
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date payTime;

    @Column(name = "issuccess")
    private String issuccess;

    @Column(name = "errorresult")
    private String errorResult;

    @Column(name = "isoffs")
    private String isoffs;

    @Column(name = "type")
    private String type;//type为0表示已发起过代扣，type为1时未发起过代扣

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



    @Column(name = "ledgerstate")
    private String ledgerState;

    public GoPayBean transToGoPayBean() {
        GoPayBean goPayBean = new GoPayBean();
        goPayBean.setContractId(contractNo);//设置合同编号
        goPayBean.setCustomerName(customerName);//设置客户名称
        goPayBean.setContractNo(contractNo);//设置合同编号
        goPayBean.setOrgManagerId(target);//设置服务费的管理公司
        goPayBean.setRePlanId("");//设置还款计划的id
        goPayBean.setSplitData1(splitData1);
        goPayBean.setSplitData2(splitData2);
        goPayBean.setBusiId("");
        goPayBean.setOrdId(MerSeq.tickOrder());
        String amount1 = splitData1.toString();
        String amount2 = splitData2.toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        goPayBean.setOrdAmt(m1 + m2 + "");

//        goPayBean.setOrdAmt("2");
//        goPayBean.setParam1("0410");//开户行号
//        goPayBean.setParam2("0");//卡折标志
//        goPayBean.setParam3("6216261000000000018");//卡号/折号
//        goPayBean.setParam4("全渠道");//持卡人姓名
//        goPayBean.setParam5("01");//证件类型
//        goPayBean.setParam6("341126197709218366"); //证件号
        goPayBean.setParam1(param1);//开户行号
        goPayBean.setParam2(param2);//卡折标志
        goPayBean.setParam3(param3);//卡号/折号
        goPayBean.setParam4(param4);//持卡人姓名
        goPayBean.setParam5(param5);//证件类型
        goPayBean.setParam6(param6); //证件号
        goPayBean.setParam7("");
        goPayBean.setParam8("");
        goPayBean.setParam9("");
        goPayBean.setParam10("");
        goPayBean.setOrdDesc("银联");
        goPayBean.setShareData(this.getShareData(m1, m2));
        goPayBean.setPriv1("");
        goPayBean.setCustomIp("");
        return goPayBean;
    }

//    private String transFwfCode() {
//        //处理服务费管理公司
//        if (StringUtils.isNotBlank(target) && "铠岳".equals(target)) {
//            return "00145112";
//        } else {
//            return "00160808";
//        }
//    }

    private String getShareData(int m1, int m2) {
        String shareData = "00145111^" + m1;
        if (StringUtils.isNotBlank(target) && m2 != 0) {
            shareData += ";" + target + "^" + m2 + ";";
        }
        // shareData = "00010001^1;00010002^1";
        return shareData;
    }

    public DataContent transToDataContent() {
        DataContent dataContent = new DataContent();
        dataContent.setAccNo(param3);
        dataContent.setIdCardType("01");
        dataContent.setIdCard(param6);
        dataContent.setIdHolder(customerName);
        // TODO: 2018-01-05 手机号码
        dataContent.setMobile("13008338782");//
        dataContent.setValidDate("");
        dataContent.setValidNo("");
        dataContent.setTransId(MerSeq.tickOrder());
        dataContent.setTxnAmt(splitData1.add(splitData2).multiply(BigDecimal.valueOf(100)).setScale(0).toString());
        dataContent.setTradeDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        dataContent.setAdditionalInfo("");
        dataContent.setReqReserved("");
        dataContent.setTransSerialNo("TSN" + System.currentTimeMillis());
        return dataContent;
    }


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
}
