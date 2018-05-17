package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by sili.chen on 2018/1/2
 */
@Component
@Configuration
public class UnionPayConfig {

    @Value("${unionpay.api.version}")
    private String version;

    @Value("${unionpay.api.member.id}")
    private String merId;

    @Value("${unionpay.api.kaiyue.service.member.id}")
    private String KaiyueServiceMemberId;

    @Value("${unionpay.api.runkun.service.member.id}")
    private String runkunServiceMemberId;

    @Value("${unionpay.api.cury.id}")
    private String curyId;


    @Value("${unionpay.api.bg.ret.url}")
    private String bgRetUrl;

    @Value("${unionpay.api.pg.ret.url}")
    private String pageRetUrl;

    @Value("${unionpay.api.gate.id}")
    private String gateId;

    @Value("${unionpay.api.type}")
    private String type;

    @Value("${unionpay.api.pfx.file}")
    private String pfxFile;

    @Value("${unionpay.api.cer.file}")
    private String cerFile;

    @Value("${unionpay.api.url}")
    private String url;

    @Value("${unionpay.api.sign-status.url}")
    private String signStatusUrl;

    @Value("${unionpay.api.sign-sms-code.url}")
    private String signSmsCodeUrl;

    @Value("${unionpay.api.sign.url}")
    private String signUrl;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getCuryId() {
        return curyId;
    }

    public void setCuryId(String curyId) {
        this.curyId = curyId;
    }

    public String getBgRetUrl() {
        return bgRetUrl;
    }

    public void setBgRetUrl(String bgRetUrl) {
        this.bgRetUrl = bgRetUrl;
    }

    public String getPageRetUrl() {
        return pageRetUrl;
    }

    public void setPageRetUrl(String pageRetUrl) {
        this.pageRetUrl = pageRetUrl;
    }

    public String getGateId() {
        return gateId;
    }

    public void setGateId(String gateId) {
        this.gateId = gateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getSignStatusUrl() {
        return signStatusUrl;
    }

    public void setSignStatusUrl(String signStatusUrl) {
        this.signStatusUrl = signStatusUrl;
    }

    public String getSignSmsCodeUrl() {
        return signSmsCodeUrl;
    }

    public void setSignSmsCodeUrl(String signSmsCodeUrl) {
        this.signSmsCodeUrl = signSmsCodeUrl;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public String getKaiyueServiceMemberId() {
        return KaiyueServiceMemberId;
    }

    public void setKaiyueServiceMemberId(String kaiyueServiceMemberId) {
        KaiyueServiceMemberId = kaiyueServiceMemberId;
    }

    public String getRunkunServiceMemberId() {
        return runkunServiceMemberId;
    }

    public void setRunkunServiceMemberId(String runkunServiceMemberId) {
        this.runkunServiceMemberId = runkunServiceMemberId;
    }
}
