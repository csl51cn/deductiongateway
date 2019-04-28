package org.starlightfinancial.deductiongateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by sili.chen on 2018/1/2
 */
@Component
@Configuration
public class BaofuConfig {

    /**
     * 宝付代扣 版本号
     */
    @Value("${baofu.classic.api.version}")
    private String classicVersion;
    /**
     * 宝付代扣 终端号
     */
    @Value("${baofu.classic.api.terminal.id}")
    private String classicTerminalId;

    /**
     * 宝付代扣 交易类型
     */
    @Value("${baofu.classic.api.txn.type}")
    private String classicTxnType;

    /**
     * 宝付代扣 接入类型:默认0000,表示为储蓄卡支付。
     */
    @Value("${baofu.classic.api.biz.type}")
    private String classicBizType;

    /**
     * 宝付代扣 交易子类
     */
    @Value("${baofu.classic.api.txn.sub.type}")
    private String classicTxnSubType;

    /**
     * 宝付代扣 商户号
     */
    @Value("${baofu.classic.api.member.id}")
    private String classicMemberId;

    /**
     * 宝付代扣 铠岳商户号
     */
    @Value("${baofu.classic.api.kaiyue.service.member.id}")
    private String classicKaiYueServiceMemberId;

    /**
     * 宝付代扣 润坤商户号
     */
    @Value("${baofu.classic.api.runkun.service.member.id}")
    private String classicRunKunServiceMemberId;

    /**
     * 宝付代扣 加密数据类型:json或者xml
     */
    @Value("${baofu.classic.api.data.type}")
    private String classicDataType;

    /**
     * 宝付代扣 安全标识
     */
    @Value("${baofu.classic.api.pay.cm}")
    private String classicPayCm;

    /**
     * 宝付代扣 交易地址
     */
    @Value("${baofu.classic.api.url}")
    private String classicUrl;

    /**
     * 宝付代扣 后台通知地址
     */
    @Value("${baofu.classic.api.notify.url}")
    private String classicNotifyUrl;

    /**
     * 宝付代扣 私钥密码
     */
    @Value("${baofu.classic.api.pri.key}")
    private String classicPriKey;

    /**
     * 宝付代扣 私钥文件
     */
    @Value("${baofu.classic.api.pfx.file}")
    private String classicPfxFile;

    /**
     * 宝付代扣 公钥文件
     */
    @Value("${baofu.classic.api.cer.file}")
    private String classicCerFile;


    /**
     * 宝付协议支付 商户号
     */
    @Value("${baofu.protocol.api.member.id}")
    private String protocolMemberId;

    /**
     * 宝付协议支付 铠岳商户号
     */
    @Value("${baofu.protocol.api.kaiyue.service.member.id}")
    private String protocolKaiYueMemberId;

    /**
     * 宝付协议支付 润坤商户号
     */
    @Value("${baofu.protocol.api.runkun.service.member.id}")
    private String protocolRunKunMemberId;

    /**
     * 宝付协议支付 支付交易地址
     */
    @Value("${baofu.protocol.api.url}")
    private String protocolUrl;

    /**
     * 宝付协议支付 后台通知地址
     */
    @Value("${baofu.protocol.api.notify.url}")
    private String protocolNotifyUrl;

    /**
     * 宝付协议支付 签约状态查询地址
     */
    @Value("${baofu.protocol.api.sign-status.url}")
    private String protocolSignStatusUrl;

    /**
     * 宝付协议支付 签约短信发送地址
     */
    @Value("${baofu.protocol.api.sign-sms-code.url}")
    private String protocolSignSmsCodeUrl;

    /**
     * 宝付协议支付 签约地址
     */
    @Value("${baofu.protocol.api.sign.url}")
    private String protocolSignUrl;

    /**
     * 宝付协议支付 支付结果查询地址
     */
    @Value("${baofu.protocol.api.query-result.url}")
    private String protocolQueryResultUrl;

    /**
     * (0,5000]手续费
     */
    @Value("${baofu.classic.api.less_than_included_5000.handling.charge}")
    private BigDecimal levelOne;

    /**
     * (5000,50000]手续费
     */
    @Value("${baofu.classic.api.over_and_5000_and_less_than_included_50000.handling.charge}")
    private BigDecimal levelTwo;

    /**
     * >50000手续费
     */
    @Value("${baofu.classic.api.over_and_50000.handling.charge}")
    private BigDecimal levelThree;

    /**
     * 宝付代付 商户号
     */
    @Value("${baofu.classic.api.pay.member.id}")
    private String classicPayMemberId;

    /**
     * 宝付代付 终端号
     */
    @Value("${baofu.classic.api.pay.terminal.id}")
    private String classicPayTerminalId;

    /**
     * 宝付代付接口
     */
    @Value("${baofu.classic.api.pay.loan-issue.url}")
    private String classicPayLoanIssueUrl;

    /**
     * 宝付代付结果查询接口
     */
    @Value("${baofu.classic.api.pay.loan-issue-query.url}")
    private String classicPayLoanIssueQueryUrl;


    /**
     * 宝付代付退款查询接口
     */
    @Value("${baofu.classic.api.pay.loan-issue-refund.url}")
    private String classicPayLoanIssueRefundUrl;

    /**
     * 协议支付费率
     */
    @Value("${baofu.protocol.api.charge}")
    private BigDecimal protocolCharge;

    public String getClassicVersion() {
        return classicVersion;
    }

    public void setClassicVersion(String classicVersion) {
        this.classicVersion = classicVersion;
    }

    public String getClassicTerminalId() {
        return classicTerminalId;
    }

    public void setClassicTerminalId(String classicTerminalId) {
        this.classicTerminalId = classicTerminalId;
    }

    public String getClassicTxnType() {
        return classicTxnType;
    }

    public void setClassicTxnType(String classicTxnType) {
        this.classicTxnType = classicTxnType;
    }

    public String getClassicBizType() {
        return classicBizType;
    }

    public void setClassicBizType(String classicBizType) {
        this.classicBizType = classicBizType;
    }

    public String getClassicTxnSubType() {
        return classicTxnSubType;
    }

    public void setClassicTxnSubType(String classicTxnSubType) {
        this.classicTxnSubType = classicTxnSubType;
    }

    public String getClassicMemberId() {
        return classicMemberId;
    }

    public void setClassicMemberId(String classicMemberId) {
        this.classicMemberId = classicMemberId;
    }

    public String getClassicKaiYueServiceMemberId() {
        return classicKaiYueServiceMemberId;
    }

    public void setClassicKaiYueServiceMemberId(String classicKaiYueServiceMemberId) {
        this.classicKaiYueServiceMemberId = classicKaiYueServiceMemberId;
    }

    public String getClassicRunKunServiceMemberId() {
        return classicRunKunServiceMemberId;
    }

    public void setClassicRunKunServiceMemberId(String classicRunKunServiceMemberId) {
        this.classicRunKunServiceMemberId = classicRunKunServiceMemberId;
    }

    public String getClassicDataType() {
        return classicDataType;
    }

    public void setClassicDataType(String classicDataType) {
        this.classicDataType = classicDataType;
    }

    public String getClassicPayCm() {
        return classicPayCm;
    }

    public void setClassicPayCm(String classicPayCm) {
        this.classicPayCm = classicPayCm;
    }

    public String getClassicUrl() {
        return classicUrl;
    }

    public void setClassicUrl(String classicUrl) {
        this.classicUrl = classicUrl;
    }

    public String getClassicNotifyUrl() {
        return classicNotifyUrl;
    }

    public void setClassicNotifyUrl(String classicNotifyUrl) {
        this.classicNotifyUrl = classicNotifyUrl;
    }

    public String getClassicPriKey() {
        return classicPriKey;
    }

    public void setClassicPriKey(String classicPriKey) {
        this.classicPriKey = classicPriKey;
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

    public String getProtocolMemberId() {
        return protocolMemberId;
    }

    public void setProtocolMemberId(String protocolMemberId) {
        this.protocolMemberId = protocolMemberId;
    }

    public String getProtocolKaiYueMemberId() {
        return protocolKaiYueMemberId;
    }

    public void setProtocolKaiYueMemberId(String protocolKaiYueMemberId) {
        this.protocolKaiYueMemberId = protocolKaiYueMemberId;
    }

    public String getProtocolRunKunMemberId() {
        return protocolRunKunMemberId;
    }

    public void setProtocolRunKunMemberId(String protocolRunKunMemberId) {
        this.protocolRunKunMemberId = protocolRunKunMemberId;
    }

    public String getProtocolUrl() {
        return protocolUrl;
    }

    public void setProtocolUrl(String protocolUrl) {
        this.protocolUrl = protocolUrl;
    }

    public String getProtocolNotifyUrl() {
        return protocolNotifyUrl;
    }

    public void setProtocolNotifyUrl(String protocolNotifyUrl) {
        this.protocolNotifyUrl = protocolNotifyUrl;
    }

    public String getProtocolSignStatusUrl() {
        return protocolSignStatusUrl;
    }

    public void setProtocolSignStatusUrl(String protocolSignStatusUrl) {
        this.protocolSignStatusUrl = protocolSignStatusUrl;
    }

    public String getProtocolSignSmsCodeUrl() {
        return protocolSignSmsCodeUrl;
    }

    public void setProtocolSignSmsCodeUrl(String protocolSignSmsCodeUrl) {
        this.protocolSignSmsCodeUrl = protocolSignSmsCodeUrl;
    }

    public String getProtocolSignUrl() {
        return protocolSignUrl;
    }

    public void setProtocolSignUrl(String protocolSignUrl) {
        this.protocolSignUrl = protocolSignUrl;
    }

    public String getProtocolQueryResultUrl() {
        return protocolQueryResultUrl;
    }

    public void setProtocolQueryResultUrl(String protocolQueryResultUrl) {
        this.protocolQueryResultUrl = protocolQueryResultUrl;
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


    public String getClassicPayMemberId() {
        return classicPayMemberId;
    }

    public void setClassicPayMemberId(String classicPayMemberId) {
        this.classicPayMemberId = classicPayMemberId;
    }

    public String getClassicPayTerminalId() {
        return classicPayTerminalId;
    }

    public void setClassicPayTerminalId(String classicPayTerminalId) {
        this.classicPayTerminalId = classicPayTerminalId;
    }

    public String getClassicPayLoanIssueUrl() {
        return classicPayLoanIssueUrl;
    }

    public void setClassicPayLoanIssueUrl(String classicPayLoanIssueUrl) {
        this.classicPayLoanIssueUrl = classicPayLoanIssueUrl;
    }


    public String getClassicPayLoanIssueQueryUrl() {
        return classicPayLoanIssueQueryUrl;
    }

    public void setClassicPayLoanIssueQueryUrl(String classicPayLoanIssueQueryUrl) {
        this.classicPayLoanIssueQueryUrl = classicPayLoanIssueQueryUrl;
    }

    public String getClassicPayLoanIssueRefundUrl() {
        return classicPayLoanIssueRefundUrl;
    }

    public void setClassicPayLoanIssueRefundUrl(String classicPayLoanIssueRefundUrl) {
        this.classicPayLoanIssueRefundUrl = classicPayLoanIssueRefundUrl;
    }

    public BigDecimal getProtocolCharge() {
        return protocolCharge;
    }

    public void setProtocolCharge(BigDecimal protocolCharge) {
        this.protocolCharge = protocolCharge;
    }
}
