package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.deductiongateway.utility.MerSeq;
import org.starlightfinancial.deductiongateway.utility.Utility;

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

    @Column(name = "paytime")
    @JsonFormat(pattern="yyyy/MM/dd.HH:mm:ss" ,timezone = "GMT+8")
    private Date payTime;

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

    public GoPayBean transToGoPayBean() {
        GoPayBean goPayBean = new GoPayBean();
        goPayBean.setContractId(contractNo);//设置合同编号
        goPayBean.setCustomerName(customerName);//设置客户名称
        goPayBean.setContractNo(contractNo);//设置合同编号
        goPayBean.setOrgManagerId(this.transFwfCode());//设置服务费的管理公司
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
        goPayBean.setMerId(Utility.SEND_BANK_MERID);//商户号
        goPayBean.setCuryId(Utility.SEND_BANK_CURYID);//订单交易币种
        goPayBean.setVersion(Utility.SEND_BANK_VERSION);//版本号
        goPayBean.setBgRetUrl(Utility.SEND_BANK_BGRETURL);//后台交易接收URL地址
        goPayBean.setPageRetUrl(Utility.SEND_BANK_PAGERETURL);//页面交易接收URL地址
        goPayBean.setGateId(Utility.SEND_BANK_GATEID);//支付网关号
        goPayBean.setParam1("0410");//开户行号
        goPayBean.setParam2("0");//卡折标志
        goPayBean.setParam3("6216261000000000018");//卡号/折号
        goPayBean.setParam4("全渠道");//持卡人姓名
        goPayBean.setParam5("01");//证件类型
        goPayBean.setParam6("341126197709218366"); //证件号
//            goPayBean.setParam1(mortgageDeduction.getParam1());//开户行号
//            goPayBean.setParam2(mortgageDeduction.getParam2());//卡折标志
//            goPayBean.setParam3(mortgageDeduction.getParam3());//卡号/折号
//            goPayBean.setParam4(mortgageDeduction.getParam4());//持卡人姓名
//            goPayBean.setParam5(mortgageDeduction.getParam5());//证件类型
//            goPayBean.setParam6(mortgageDeduction.getParam6()); //证件号
        goPayBean.setParam7("");
        goPayBean.setParam8("");
        goPayBean.setParam9("");
        goPayBean.setParam10("");
        goPayBean.setOrdDesc("批量代扣款");
        goPayBean.setShareType(Utility.SEND_BANK_TYPE);//分账类型
        goPayBean.setShareData(this.getShareData());
        goPayBean.setPriv1("");
        goPayBean.setCustomIp("");
        return goPayBean;
    }

    private String transFwfCode() {
        //处理服务费管理公司
        if (StringUtils.isNotBlank(target) && "铠岳".equals(target)) {
            return "00145112";
        } else {
            return "00160808";
        }
    }

    private String getShareData() {
        String shareData = "00145111^" + splitData1;
        if (StringUtils.isNotBlank(target)) {
            shareData += ";" + this.transFwfCode() + "^" + splitData2 + ";";
        }
        shareData = "00010001^1;00010002^1";
        return shareData;
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
}
