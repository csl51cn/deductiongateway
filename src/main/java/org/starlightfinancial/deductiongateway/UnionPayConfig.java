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

    @Value("${baofu.api.notify.url}")
    private String notifyUrl;

    @Value("${baofu.api.pri.key}")
    private String priKey;

    @Value("${baofu.api.pfx.file}")
    private String pfxFile;

    @Value("${baofu.api.cer.file}")
    private String cerFile;

    @Value("${unionpay.api.url}")
    private String url;

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
}
