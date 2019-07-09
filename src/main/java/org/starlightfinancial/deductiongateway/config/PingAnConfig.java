package org.starlightfinancial.deductiongateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

/**
 * @author: Senlin.Deng
 * @Description: 平安配置类
 * @date: Created in 2019/6/27 14:40
 * @Modified By:
 */
@Configuration
@Data
public class PingAnConfig {


    /**
     * 平安域账号注册接口地址
     */
    @Value("${pingan.commercial.entrust.customer.registration.url}")
    private String registrationUrl;

    /**
     * 平安商委签约状态查询接口地址
     */
    @Value("${pingan.commercial.entrust.sign-status.url}")
    private String signStatusUrl;

    /**
     * 平安商委签约接口地址
     */
    @Value("${pingan.commercial.entrust.sign.url}")
    private String signUrl;


    /**
     * 平安代扣接口地址
     */
    @Value("${pingan.commercial.entrust.pay.url}")
    private String payUrl;

    /**
     * 平安查询代扣结果接口地址
     */
    @Value("${pingan.commercial.entrust.query-result.url}")
    private String queryResultUrl;


    /**
     * 平安平台商户号:分账用
     */
    @Value("${pingan.commercial.entrust.platMerchantId}")
    private String platMerchantId;

    /**
     * 平安中间商户号:创建订单用
     */
    @Value("${pingan.commercial.entrust.merchantId}")
    private String merchantId;
    /**
     * 平安050商委签约后台通知地址
     */
    @Value("${pingan.commercial.entrust.sign-status.backend.url}")
    private String signStatusBackEndUrl;

    /**
     * 平安001合壹付支付后台通知地址
     */
    @Value("${pingan.commercial.entrust.pay.backend.url}")
    private String payBackEndUrl;

    /**
     * 平安商委手续费
     */
    @Value("${pingan.commercial.entrust.charge}")
    private BigDecimal charge;
}
