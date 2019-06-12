package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付配置
 * @date: Created in 2019/4/16 15:47
 * @Modified By:
 */
@Configuration
public class ChinaPayClearNetConfig {

    /**
     * 商户号
     */
    @Value("${cpcn.classic.api.member.id}")
    private String classicMemberId;

    /**
     * 单笔代扣地址
     */
    @Value("${cpcn.classic.api.deduction.url}")
    private String classicDeductionUrl;

    /**
     * 单笔代扣结果查询地址
     */
    @Value("${cpcn.classic.api.query-result.url}")
    private String classicQueryResultUrl;


    /**
     * 单笔代扣(0,20000]手续费
     */
    @Value("${cpcn.classic.api.less_than_included_20000.handling.charge}")
    private BigDecimal levelOne;

    /**
     * 单笔代扣(20000,∞)手续费
     */
    @Value("${cpcn.classic.api.over_20000.handling.charge}")
    private BigDecimal levelTwo;

    /**
     * 快捷支付签约状态查询地址
     */
    @Value("${cpcn.quick.realtime.api.sign-status.url}")
    private String quickRealTimeSignStatusUrl;


    /**
     * 快捷支付签约短信发送地址
     */
    @Value("${cpcn.quick.realtime.api.sign-sms-code.url}")
    private String quickRealTimeSignSmsCodeUrl;

    /**
     * 快捷支付签约地址
     */
    @Value("${cpcn.quick.realtime.api.sign.url}")
    private String quickRealTimeSignUrl;

    /**
     * 快捷支付交易地址
     */
    @Value("${cpcn.quick.realtime.api.url}")
    private String quickRealTimeUrl;

    /**
     * 快捷支付支付结果查询
     */
    @Value("${cpcn.quick.realtime.api.query-result.url}")
    private String quickRealTimeQueryResultUrl;

    /**
     * 快捷支付费率
     */
    @Value("${cpcn.quick.realtime.api.charge}")
    private BigDecimal quickRealTimeCharge;


    /**
     * 工行 单笔(2011)和批量(1610)代扣费率
     */
    @Value("${cpcn.classic.api.ICBC.handling.charge}")
    private BigDecimal icbcCharge;


    /**
     * 建行 单笔(2011)和批量(1610)代扣费率
     */
    @Value("${cpcn.classic.api.CCB.handling.charge}")
    private BigDecimal ccbCharge;

    public String getClassicMemberId() {
        return classicMemberId;
    }

    public void setClassicMemberId(String classicMemberId) {
        this.classicMemberId = classicMemberId;
    }

    public String getClassicDeductionUrl() {
        return classicDeductionUrl;
    }

    public void setClassicDeductionUrl(String classicDeductionUrl) {
        this.classicDeductionUrl = classicDeductionUrl;
    }

    public String getClassicQueryResultUrl() {
        return classicQueryResultUrl;
    }

    public void setClassicQueryResultUrl(String classicQueryResultUrl) {
        this.classicQueryResultUrl = classicQueryResultUrl;
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

    public String getQuickRealTimeSignStatusUrl() {
        return quickRealTimeSignStatusUrl;
    }

    public void setQuickRealTimeSignStatusUrl(String quickRealTimeSignStatusUrl) {
        this.quickRealTimeSignStatusUrl = quickRealTimeSignStatusUrl;
    }

    public String getQuickRealTimeSignSmsCodeUrl() {
        return quickRealTimeSignSmsCodeUrl;
    }

    public void setQuickRealTimeSignSmsCodeUrl(String quickRealTimeSignSmsCodeUrl) {
        this.quickRealTimeSignSmsCodeUrl = quickRealTimeSignSmsCodeUrl;
    }

    public String getQuickRealTimeSignUrl() {
        return quickRealTimeSignUrl;
    }

    public void setQuickRealTimeSignUrl(String quickRealTimeSignUrl) {
        this.quickRealTimeSignUrl = quickRealTimeSignUrl;
    }

    public String getQuickRealTimeUrl() {
        return quickRealTimeUrl;
    }

    public void setQuickRealTimeUrl(String quickRealTimeUrl) {
        this.quickRealTimeUrl = quickRealTimeUrl;
    }

    public String getQuickRealTimeQueryResultUrl() {
        return quickRealTimeQueryResultUrl;
    }

    public void setQuickRealTimeQueryResultUrl(String quickRealTimeQueryResultUrl) {
        this.quickRealTimeQueryResultUrl = quickRealTimeQueryResultUrl;
    }

    public BigDecimal getQuickRealTimeCharge() {
        return quickRealTimeCharge;
    }

    public void setQuickRealTimeCharge(BigDecimal quickRealTimeCharge) {
        this.quickRealTimeCharge = quickRealTimeCharge;
    }

    public BigDecimal getIcbcCharge() {
        return icbcCharge;
    }

    public void setIcbcCharge(BigDecimal icbcCharge) {
        this.icbcCharge = icbcCharge;
    }

    public BigDecimal getCcbCharge() {
        return ccbCharge;
    }

    public void setCcbCharge(BigDecimal ccbCharge) {
        this.ccbCharge = ccbCharge;
    }
}
