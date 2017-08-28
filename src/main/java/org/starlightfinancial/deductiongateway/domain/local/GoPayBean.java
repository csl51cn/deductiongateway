package org.starlightfinancial.deductiongateway.domain.local;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GoPayBean {
    private String MerId;
    private String BusiId;
    private String OrdId;
    private String OrdAmt;
    private String CuryId;
    private String Version;
    private String BgRetUrl;
    private String PageRetUrl;
    private String GateId;
    private String Param1;
    private String Param2;
    private String Param3;
    private String Param4;
    private String Param5;
    private String Param6;
    private String Param7;
    private String Param8;
    private String Param9;
    private String Param10;
    private String OrdDesc;
    private String ShareType;
    private String ShareData;
    private String Priv1;
    private String payStat;
    private String payTime;
    private String CustomIp;
    private String ChkValue;
    private String contractId;
    private String customerNo;
    private String customerName;
    private String contractNo;
    private String orgManagerId;
    private String rePlanId;


    private BigDecimal splitData1;
    private BigDecimal splitData2;

    public String getCustomIp() {
        return this.CustomIp;
    }

    public void setCustomIp(String customIp) {
        this.CustomIp = customIp;
    }

    public String getPayStat() {
        return this.payStat;
    }

    public void setPayStat(String payStat) {
        this.payStat = payStat;
    }

    public String getPayTime() {
        return this.payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getOrdDesc() {
        return this.OrdDesc;
    }

    public void setOrdDesc(String ordDesc) {
        this.OrdDesc = ordDesc;
    }

    public String getShareType() {
        return this.ShareType;
    }

    public void setShareType(String shareType) {
        this.ShareType = shareType;
    }

    public String getChkValue() {
        return this.ChkValue;
    }

    public void setChkValue(String chkValue) {
        this.ChkValue = chkValue;
    }

    public String getMerId() {
        return this.MerId;
    }

    public void setMerId(String merId) {
        this.MerId = merId;
    }

    public String getBusiId() {
        return this.BusiId;
    }

    public void setBusiId(String busiId) {
        this.BusiId = busiId;
    }

    public String getOrdId() {
        return this.OrdId;
    }

    public void setOrdId(String ordId) {
        this.OrdId = ordId;
    }

    public String getOrdAmt() {
        return this.OrdAmt;
    }

    public void setOrdAmt(String ordAmt) {
        this.OrdAmt = ordAmt;
    }

    public String getCuryId() {
        return this.CuryId;
    }

    public void setCuryId(String curyId) {
        this.CuryId = curyId;
    }

    public String getVersion() {
        return this.Version;
    }

    public void setVersion(String version) {
        this.Version = version;
    }

    public String getBgRetUrl() {
        return this.BgRetUrl;
    }

    public void setBgRetUrl(String bgRetUrl) {
        this.BgRetUrl = bgRetUrl;
    }

    public String getPageRetUrl() {
        return this.PageRetUrl;
    }

    public void setPageRetUrl(String pageRetUrl) {
        this.PageRetUrl = pageRetUrl;
    }

    public String getGateId() {
        return this.GateId;
    }

    public void setGateId(String gateId) {
        this.GateId = gateId;
    }

    public String getParam1() {
        return this.Param1;
    }

    public void setParam1(String param1) {
        this.Param1 = param1;
    }

    public String getParam2() {
        return this.Param2;
    }

    public void setParam2(String param2) {
        this.Param2 = param2;
    }

    public String getParam3() {
        return this.Param3;
    }

    public void setParam3(String param3) {
        this.Param3 = param3;
    }

    public String getParam4() {
        return this.Param4;
    }

    public void setParam4(String param4) {
        this.Param4 = param4;
    }

    public String getParam5() {
        return this.Param5;
    }

    public void setParam5(String param5) {
        this.Param5 = param5;
    }

    public String getParam6() {
        return this.Param6;
    }

    public void setParam6(String param6) {
        this.Param6 = param6;
    }

    public String getParam7() {
        return this.Param7;
    }

    public void setParam7(String param7) {
        this.Param7 = param7;
    }

    public String getParam8() {
        return this.Param8;
    }

    public void setParam8(String param8) {
        this.Param8 = param8;
    }

    public String getParam9() {
        return this.Param9;
    }

    public void setParam9(String param9) {
        this.Param9 = param9;
    }

    public String getParam10() {
        return this.Param10;
    }

    public void setParam10(String param10) {
        this.Param10 = param10;
    }

    public String getShareData() {
        return this.ShareData;
    }

    public void setShareData(String shareData) {
        this.ShareData = shareData;
    }

    public String getPriv1() {
        return this.Priv1;
    }

    public void setPriv1(String priv1) {
        this.Priv1 = priv1;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer sb = new StringBuffer();
        Field[] fields = this.getClass().getDeclaredFields();
        sb.append(this.getClass().getName() + "{");
        for (int i = 0; i < fields.length; i++) {
            try {
                if (fields[i].get(this) instanceof Date) {
                    if (fields[i].get(this) != null) {
                        sb.append(fields[i].getName() + ":").append(
                                sdf.format(fields[i].get(this))).append(";");
                        continue;
                    }
                }
                sb.append(fields[i].getName()).append(":").append(
                        fields[i].get(this)).append(";");
            } catch (IllegalArgumentException e) {
            } catch (IllegalAccessException e) {
            }
        }
        sb.append("}");
        return sb.toString();
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

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
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

    public String getOrgManagerId() {
        return orgManagerId;
    }

    public void setOrgManagerId(String orgManagerId) {
        this.orgManagerId = orgManagerId;
    }

    public String getRePlanId() {
        return rePlanId;
    }

    public void setRePlanId(String rePlanId) {
        this.rePlanId = rePlanId;
    }

}
