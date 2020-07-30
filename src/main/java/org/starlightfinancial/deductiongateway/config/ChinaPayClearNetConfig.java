package org.starlightfinancial.deductiongateway.config;

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
     * 除中国银行外的费率
     */
    @Value("${cpcn.classic.api.charge}")
    private BigDecimal classicCharge;

    /**
     * 封底费用
     */
    @Value("${cpcn.classic.api.lowest.charge}")
    private BigDecimal classicLowestCharge;

    /**
     * 中国银行费率
     */
    @Value("${cpcn.classic.api.BOC.charge}")
    private BigDecimal bocCharge;

    public BigDecimal getBocCharge() {
        return bocCharge;
    }

    public void setBocCharge(BigDecimal bocCharge) {
        this.bocCharge = bocCharge;
    }

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

    public BigDecimal getClassicCharge() {
        return classicCharge;
    }

    public void setClassicCharge(BigDecimal classicCharge) {
        this.classicCharge = classicCharge;
    }

    public BigDecimal getClassicLowestCharge() {
        return classicLowestCharge;
    }

    public void setClassicLowestCharge(BigDecimal classicLowestCharge) {
        this.classicLowestCharge = classicLowestCharge;
    }
}
