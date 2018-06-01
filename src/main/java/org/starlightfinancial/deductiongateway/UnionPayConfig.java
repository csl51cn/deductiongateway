package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by sili.chen on 2018/1/2
 */
@Component
public class UnionPayConfig {


    @Value("${unionpay.api.member.id}")
    private String merId;

    @Value("${unionpay.api.kaiyue.service.member.id}")
    private String KaiyueServiceMemberId;

    @Value("${unionpay.api.runkun.service.member.id}")
    private String runkunServiceMemberId;

    @Value("${unionpay.api.cury.id}")
    private String curyId;

    @Value("${unionpay.api.url}")
    private String url;

    @Value("${unionpay.api.sign-status.url}")
    private String signStatusUrl;

    @Value("${unionpay.api.sign-sms-code.url}")
    private String signSmsCodeUrl;

    @Value("${unionpay.api.sign.url}")
    private String signUrl;

    @Value("${unionpay.api.query-result.url}")
    private  String   queryResultUrl;


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

    public String getQueryResultUrl() {
        return queryResultUrl;
    }

    public void setQueryResultUrl(String queryResultUrl) {
        this.queryResultUrl = queryResultUrl;
    }
}
