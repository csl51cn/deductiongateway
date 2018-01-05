
package org.starlightfinancial.deductiongateway.domain.remote;

import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.starlightfinancial.deductiongateway.baofu.domain.DataContent;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.utility.MerSeq;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sili.chen on 2017/8/25
 */
@Entity
public class AutoBatchDeduction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "dateid")
    private Integer dateId;

    @Column(name = "业务编号")
    private String busiNo;

    @Column(name = "还款账号银行")
    @NotEmpty(message = "还款账号银行不能为空")
    private String bankName;

    @Column(name = "代扣卡折类型")
    @NotEmpty(message = "代扣卡折类型不能为空")
    private String cardAndPassbook;

    @Column(name = "还款账户名")
    @NotEmpty(message = "还款账户名不能为空")
    private String customerName;

    @Column(name = "还款账号")
    @NotEmpty(message = "还款账号不能为空")
    private String accout;

    @Column(name = "代扣人证件类型")
    @NotEmpty(message = "代扣人证件类型不能为空")
    private String certificateType;

    @Column(name = "代扣人证件号码")
    @NotEmpty(message = "代扣人证件号码不能为空")
    private String certificateNo;

    @Column(name = "计划期数")
    private String planVolume;

    @Column(name = "计划还款日")
    private Date planDate;

    @Column(name = "当期应还本息")
    @DecimalMin(value = "0", message = "当期应还本息不能小于0")
    private BigDecimal bxAmount;

    @Column(name = "当期应还服务费")
    @DecimalMin(value = "0", message = "当期应还服务费不能小于0")
    private BigDecimal fwfAmount;

    @Column(name = "服务费管理司")
    @NotEmpty(message = "服务费管理司不能为空")
    private String fwfCompamny;

    @Column(name = "合同编号")
    @NotEmpty(message = "合同编号不能为空")
    private String contractNo;

    public GoPayBean transToGoPayBean() {
        GoPayBean goPayBean = new GoPayBean();
        goPayBean.setContractId(contractNo);//设置合同编号
        goPayBean.setCustomerName(customerName);//设置客户名称
        goPayBean.setContractNo(contractNo);//设置合同编号
        goPayBean.setOrgManagerId(this.transFwfCode());//设置服务费的管理公司
        goPayBean.setRePlanId("");//设置还款计划的id
        goPayBean.setSplitData1(bxAmount);
        goPayBean.setSplitData2(fwfAmount);
        goPayBean.setBusiId("");
        String amount1 = bxAmount.toString();
        String amount2 = fwfAmount.toString();
        int m1 = 0;
        if (StringUtils.isNotBlank(amount1)) {
            m1 = new BigDecimal(amount1).movePointRight(2).intValue();
        }
        int m2 = 0;
        if (StringUtils.isNotBlank(amount2)) {
            m2 = new BigDecimal(amount2).movePointRight(2).intValue();
        }
        goPayBean.setOrdAmt(m1 + m2 + "");
        goPayBean.setOrdId(MerSeq.tickOrder());
        //  goPayBean.setOrdAmt("2");
//        goPayBean.setParam1("0410");//开户行号
//        goPayBean.setParam2("0");//卡折标志
//        goPayBean.setParam3("6216261000000000018");//卡号/折号
//        goPayBean.setParam4("全渠道");//持卡人姓名
//        goPayBean.setParam5("01");//证件类型
//        goPayBean.setParam6("341126197709218366"); //证件号
        goPayBean.setParam1(bankName);//开户行号
        goPayBean.setParam2("0");//卡折标志
        goPayBean.setParam3(accout);//卡号/折号
        goPayBean.setParam4(customerName);//持卡人姓名
        goPayBean.setParam5(certificateType);//证件类型
        goPayBean.setParam6(certificateNo); //证件号
        goPayBean.setParam7("");
        goPayBean.setParam8("");
        goPayBean.setParam9("");
        goPayBean.setParam10("");
        goPayBean.setOrdDesc("银联");
        goPayBean.setShareData(this.getShareData(m1, m2));
        goPayBean.setPriv1("");
        goPayBean.setCustomIp("");
        goPayBean.setPayStat("");
        goPayBean.setPayTime("");
        return goPayBean;
    }

    public DataContent transToDataContent() {
        DataContent dataContent = new DataContent();
        dataContent.setAccNo(accout);
        dataContent.setIdCardType("01");
        dataContent.setIdCard(certificateNo);
        dataContent.setIdHolder(customerName);
        // TODO: 2018/1/5 电话号码不能为空，否则会报交易要素缺失，但是测试环境送错误的号码也可以通过交易
        dataContent.setMobile("13999999999");
        dataContent.setValidDate("");
        dataContent.setValidNo("");
        dataContent.setTransId(MerSeq.tickOrder());
        dataContent.setTxnAmt(bxAmount.add(fwfAmount).multiply(BigDecimal.valueOf(100)).setScale(0).toString());
        dataContent.setTradeDate(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        dataContent.setAdditionalInfo("");
        dataContent.setReqReserved("");
        dataContent.setTransSerialNo("TSN" + System.currentTimeMillis());
        // TODO: 2018/1/5 有分账账户但分账金额为0，交易不能通过
        dataContent.setShareInfo("100000749," + bxAmount.multiply(BigDecimal.valueOf(100)).setScale(0).toString()
                + ";100000178," + fwfAmount.multiply(BigDecimal.valueOf(100)).setScale(0).toString());
        // TODO: 2018/1/5 分账手续费账户可以不填
        dataContent.setFeeMemberId("100000749");
        return dataContent;
    }

    private String transFwfCode() {
        //处理服务费管理公司
        if (StringUtils.isNotBlank(fwfCompamny) && "铠岳".equals(fwfCompamny)) {
            return "00145112";
        } else {
            return "00160808";
        }
    }

    private String getShareData(int m1, int m2) {
        String shareData = "00145111^" + m1;
        if (StringUtils.isNotBlank(fwfCompamny) && m2 != 0) {
            shareData += ";" + this.transFwfCode() + "^" + m2 + ";";
        }
//        shareData = "00010001^1;00010002^1";
        return shareData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public String getBusiNo() {
        return busiNo;
    }

    public void setBusiNo(String busiNo) {
        this.busiNo = busiNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardAndPassbook() {
        return cardAndPassbook;
    }

    public void setCardAndPassbook(String cardAndPassbook) {
        this.cardAndPassbook = cardAndPassbook;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAccout() {
        return accout;
    }

    public void setAccout(String accout) {
        this.accout = accout;
    }

    public String getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(String certificateType) {
        this.certificateType = certificateType;
    }

    public String getCertificateNo() {
        return certificateNo;
    }

    public void setCertificateNo(String certificateNo) {
        this.certificateNo = certificateNo;
    }

    public String getPlanVolume() {
        return planVolume;
    }

    public void setPlanVolume(String planVolume) {
        this.planVolume = planVolume;
    }

    public Date getPlanDate() {
        return planDate;
    }

    public void setPlanDate(Date planDate) {
        this.planDate = planDate;
    }

    public BigDecimal getBxAmount() {
        return bxAmount;
    }

    public void setBxAmount(BigDecimal bxAmount) {
        this.bxAmount = bxAmount;
    }

    public BigDecimal getFwfAmount() {
        return fwfAmount;
    }

    public void setFwfAmount(BigDecimal fwfAmount) {
        this.fwfAmount = fwfAmount;
    }

    public String getFwfCompamny() {
        return fwfCompamny;
    }

    public void setFwfCompamny(String fwfCompamny) {
        this.fwfCompamny = fwfCompamny;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    @Override
    public String toString() {
        return "AutoBatchDeduction{" +
                "id=" + id +
                ", dateId=" + dateId +
                ", busiNo='" + busiNo + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardAndPassbook='" + cardAndPassbook + '\'' +
                ", customerName='" + customerName + '\'' +
                ", accout='" + accout + '\'' +
                ", certificateType='" + certificateType + '\'' +
                ", certificateNo='" + certificateNo + '\'' +
                ", planVolume='" + planVolume + '\'' +
                ", planDate=" + planDate +
                ", bxAmount=" + bxAmount +
                ", fwfAmount=" + fwfAmount +
                ", fwfCompamny='" + fwfCompamny + '\'' +
                ", contractNo='" + contractNo + '\'' +
                '}';
    }
}
