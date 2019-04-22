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

    @Value("${cpcn.classic.api.member.id}")
    private String classicMemberId;

    @Value("${cpcn.classic.api.deduction.url}")
    private String  classicDeductionUrl;

    @Value("${cpcn.classic.api.query-result.url}")
    private String classicQueryResultUrl;


    /**
     * (0,20000]手续费
     */
    @Value("${cpcn.classic.api.less_than_included_20000.handling.charge}")
    private BigDecimal levelOne;

    /**
     * (20000,∞)手续费
     */
    @Value("${cpcn.classic.api.over_20000.handling.charge}")
    private BigDecimal levelTwo;


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
}
