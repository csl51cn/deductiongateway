package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by sili.chen on 2018/1/2
 */
@Component
@Configuration
public class BaofuConfig {

    @Value("${baofu.api.version}")
    private String version;

    @Value("${baofu.api.terminal.id}")
    private String terminalId;

    @Value("${baofu.api.txn.type}")
    private String txnType;

    @Value("${baofu.api.txn.sub.type}")
    private String txnSubType;

    @Value("${baofu.api.member.id}")
    private String memberId;


    @Value("${baofu.api.service.member.id}")
    private String serviceMemberId;

    @Value("${baofu.api.data.type}")
    private String dataType;

    @Value("${baofu.api.biz.type}")
    private String bizType;

    @Value("${baofu.api.pay.cm}")
    private String payCm;

    @Value("${baofu.api.notify.url}")
    private String notifyUrl;

    @Value("${baofu.api.pri.key}")
    private String priKey;

    @Value("${baofu.api.pfx.file}")
    private String pfxFile;

    @Value("${baofu.api.cer.file}")
    private String cerFile;

    @Value("${baofu.api.url}")
    private String url;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getTxnSubType() {
        return txnSubType;
    }

    public void setTxnSubType(String txnSubType) {
        this.txnSubType = txnSubType;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public String getPayCm() {
        return payCm;
    }

    public void setPayCm(String payCm) {
        this.payCm = payCm;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPriKey() {
        return priKey;
    }

    public void setPriKey(String priKey) {
        this.priKey = priKey;
    }

    public String getPfxFile() {
        return pfxFile;
    }

    public void setPfxFile(String pfxFile) {
        this.pfxFile = pfxFile;
    }

    public String getCerFile() {
        return cerFile;
    }

    public void setCerFile(String cerFile) {
        this.cerFile = cerFile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getServiceMemberId() {
        return serviceMemberId;
    }

    public void setServiceMemberId(String serviceMemberId) {
        this.serviceMemberId = serviceMemberId;
    }
}
