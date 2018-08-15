package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: Senlin.Deng
 * @Description: 银联配置
 * @date: Created in 2018/5/29 13:40
 * @Modified By:
 */
@Component
public class ChinaPayConfig {

    /**
     * 快捷支付交易地址
     */
    @Value("${chinapay.express.realtime.api.url}")
    private String expressRealTimeUrl;

    /**
     * 快捷支付商户号
     */
    @Value("${chinapay.express.realtime.api.member.id}")
    private String expressRealTimeMemberId;


    /**
     * 快捷支付铠岳商户号
     */
    @Value("${chinapay.express.realtime.api.kaiyue.service.member.id}")
    private String expressRealTimeKaiYueServiceMemberId;

    /**
     * 快捷支付铠岳商户号
     */
    @Value("${chinapay.express.realtime.api.runtong.member.id}")
    private String expressRealTimeRunTongMemberId;

    /**
     * 快捷支付润坤商户号
     */
    @Value("${chinapay.express.realtime.api.runkun.service.member.id}")
    private String expressRealTimeRunKunServiceMemberId;

    /**
     * 货币类型
     */
    @Value("${chinapay.express.api.cury.id}")
    private String curyId;

    /**
     * 快捷支付签约状态查询地址
     */
    @Value("${chinapay.express.realtime.api.sign-status.url}")
    private String expressRealtimeSignStatusUrl;

    /**
     * 快捷支付签约短信发送地址
     */
    @Value("${chinapay.express.realtime.api.sign-sms-code.url}")
    private String expressRealTimeSignSmsCodeUrl;

    /**
     * 快捷支付签约地址
     */
    @Value("${chinapay.express.realtime.api.sign.url}")
    private String expressRealTimeSignUrl;

    /**
     * 快捷支付支付结果查询
     */
    @Value("${chinapay.express.realtime.api.query-result.url}")
    private String expressRealTimeQueryResultUrl;

    /**
     * 新无卡代扣地址
     */
    @Value("${chinapay.express.delay.api.url}")
    private String expressDelayUrl;


    /**
     * 白名单代扣模式版本号
     */
    @Value("${chinapay.classic.api.version}")
    private String classicVersion;

    /**
     * 白名单代扣地址
     */
    @Value("${chinapay.classic.api.url}")
    private String classicUrl;


    /**
     * 白名单代扣私钥
     */
    @Value("${chinapay.classic.api.pfx.file}")
    private String classicPfxFile;

    /**
     * 白名单代扣公钥
     */
    @Value("${chinapay.classic.api.cer.file}")
    private String classicCerFile;

    /**
     * 白名单代扣商户号
     */
    @Value("${chinapay.classic.api.member.id}")
    private String classicMerId;

    /**
     * 白名单代扣润通分账商户号
     */
    @Value("${chinapay.classic.api.runtong.member.id}")
    private String classicRunTongMemberId;

    /**
     * 白名单代扣铠岳分账商户号
     */
    @Value("${chinapay.classic.api.kaiyue.service.member.id}")
    private String classicKaiYueMemberId;

    /**
     * 白名单代扣润坤分账商户号
     */
    @Value("${chinapay.classic.api.runkun.service.member.id}")
    private String classicRunKunMemberId;

    /**
     * 白名单代扣支付网关号
     */
    @Value("${chinapay.classic.api.gate.id}")
    private String classicGateId;

    /**
     * 白名单代扣分账类型:0001为即时分帐0002为延时分帐
     */
    @Value("${chinapay.classic.api.type}")
    private String classicType;

    /**
     * 白名单代扣:后台交易接收URL
     */
    @Value("${chinapay.classic.api.bg.ret.url}")
    private String classicBgRetUrl;

    /**
     * 白名单代扣:页面交易接收URL，用于引导使用者返回支付后的商户网站页面
     */
    @Value("${chinapay.classic.api.pg.ret.url}")
    private String classicPageRetUrl;


    /**
     * (0,1000)手续费
     */
    @Value("${chinapay.classic.api.less_than_1000.handling.charge}")
    private BigDecimal levelOne;

    /**
     * [1000,5000)手续费
     */
    @Value("${chinapay.classic.api.over_and_included_1000_and_less_than_5000.handling.charge}")
    private BigDecimal levelTwo;

    /**
     * ≥5000手续费
     */
    @Value("${chinapay.classic.api.over_and_included_5000.handling.charge}")
    private BigDecimal levelThree;


    public String getExpressRealTimeUrl() {
        return expressRealTimeUrl;
    }

    public void setExpressRealTimeUrl(String expressRealTimeUrl) {
        this.expressRealTimeUrl = expressRealTimeUrl;
    }

    public String getExpressRealTimeMemberId() {
        return expressRealTimeMemberId;
    }

    public void setExpressRealTimeMemberId(String expressRealTimeMemberId) {
        this.expressRealTimeMemberId = expressRealTimeMemberId;
    }

    public String getExpressRealTimeKaiYueServiceMemberId() {
        return expressRealTimeKaiYueServiceMemberId;
    }

    public void setExpressRealTimeKaiYueServiceMemberId(String expressRealTimeKaiYueServiceMemberId) {
        this.expressRealTimeKaiYueServiceMemberId = expressRealTimeKaiYueServiceMemberId;
    }

    public String getExpressRealTimeRunKunServiceMemberId() {
        return expressRealTimeRunKunServiceMemberId;
    }

    public void setExpressRealTimeRunKunServiceMemberId(String expressRealTimeRunKunServiceMemberId) {
        this.expressRealTimeRunKunServiceMemberId = expressRealTimeRunKunServiceMemberId;
    }

    public String getCuryId() {
        return curyId;
    }

    public void setCuryId(String curyId) {
        this.curyId = curyId;
    }

    public String getExpressRealtimeSignStatusUrl() {
        return expressRealtimeSignStatusUrl;
    }

    public void setExpressRealtimeSignStatusUrl(String expressRealtimeSignStatusUrl) {
        this.expressRealtimeSignStatusUrl = expressRealtimeSignStatusUrl;
    }

    public String getExpressRealTimeSignSmsCodeUrl() {
        return expressRealTimeSignSmsCodeUrl;
    }

    public void setExpressRealTimeSignSmsCodeUrl(String expressRealTimeSignSmsCodeUrl) {
        this.expressRealTimeSignSmsCodeUrl = expressRealTimeSignSmsCodeUrl;
    }

    public String getExpressRealTimeSignUrl() {
        return expressRealTimeSignUrl;
    }

    public void setExpressRealTimeSignUrl(String expressRealTimeSignUrl) {
        this.expressRealTimeSignUrl = expressRealTimeSignUrl;
    }

    public String getExpressRealTimeQueryResultUrl() {
        return expressRealTimeQueryResultUrl;
    }

    public void setExpressRealTimeQueryResultUrl(String expressRealTimeQueryResultUrl) {
        this.expressRealTimeQueryResultUrl = expressRealTimeQueryResultUrl;
    }

    public String getExpressDelayUrl() {
        return expressDelayUrl;
    }

    public void setExpressDelayUrl(String expressDelayUrl) {
        this.expressDelayUrl = expressDelayUrl;
    }

    public String getClassicVersion() {
        return classicVersion;
    }

    public void setClassicVersion(String classicVersion) {
        this.classicVersion = classicVersion;
    }

    public String getClassicUrl() {
        return classicUrl;
    }

    public void setClassicUrl(String classicUrl) {
        this.classicUrl = classicUrl;
    }

    public String getClassicPfxFile() {
        return classicPfxFile;
    }

    public void setClassicPfxFile(String classicPfxFile) {
        this.classicPfxFile = classicPfxFile;
    }

    public String getClassicCerFile() {
        return classicCerFile;
    }

    public void setClassicCerFile(String classicCerFile) {
        this.classicCerFile = classicCerFile;
    }

    public String getClassicMerId() {
        return classicMerId;
    }

    public void setClassicMerId(String classicMerId) {
        this.classicMerId = classicMerId;
    }

    public String getClassicRunTongMemberId() {
        return classicRunTongMemberId;
    }

    public void setClassicRunTongMemberId(String classicRunTongMemberId) {
        this.classicRunTongMemberId = classicRunTongMemberId;
    }

    public String getClassicKaiYueMemberId() {
        return classicKaiYueMemberId;
    }

    public void setClassicKaiYueMemberId(String classicKaiYueMemberId) {
        this.classicKaiYueMemberId = classicKaiYueMemberId;
    }

    public String getClassicRunKunMemberId() {
        return classicRunKunMemberId;
    }

    public void setClassicRunKunMemberId(String classicRunKunMemberId) {
        this.classicRunKunMemberId = classicRunKunMemberId;
    }

    public String getClassicGateId() {
        return classicGateId;
    }

    public void setClassicGateId(String classicGateId) {
        this.classicGateId = classicGateId;
    }

    public String getClassicType() {
        return classicType;
    }

    public void setClassicType(String classicType) {
        this.classicType = classicType;
    }

    public String getClassicBgRetUrl() {
        return classicBgRetUrl;
    }

    public void setClassicBgRetUrl(String classicBgRetUrl) {
        this.classicBgRetUrl = classicBgRetUrl;
    }

    public String getClassicPageRetUrl() {
        return classicPageRetUrl;
    }

    public void setClassicPageRetUrl(String classicPageRetUrl) {
        this.classicPageRetUrl = classicPageRetUrl;
    }

    public String getExpressRealTimeRunTongMemberId() {
        return expressRealTimeRunTongMemberId;
    }

    public void setExpressRealTimeRunTongMemberId(String expressRealTimeRunTongMemberId) {
        this.expressRealTimeRunTongMemberId = expressRealTimeRunTongMemberId;
    }

    public BigDecimal getLevelOne() {
        return levelOne;
    }

    public void setLevelOne(BigDecimal levelOne) {
        this.levelOne = levelOne;
    }

    public BigDecimal getLevelTwo() {
        return levelTwo;
    }

    public void setLevelTwo(BigDecimal levelTwo) {
        this.levelTwo = levelTwo;
    }

    public BigDecimal getLevelThree() {
        return levelThree;
    }

    public void setLevelThree(BigDecimal levelThree) {
        this.levelThree = levelThree;
    }
}
