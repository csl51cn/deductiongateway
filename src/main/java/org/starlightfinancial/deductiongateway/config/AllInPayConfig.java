package org.starlightfinancial.deductiongateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * @author: Senlin.Deng
 * @Description: 通联收银宝
 * @date: Created in 2018/8/10 11:32
 * @Modified By:
 */
@Component
public class AllInPayConfig {

    /**
     * 手续费费率
     */
    @Value("${allinpay.classic.api.handling.charge.rate}")
    private BigDecimal handlingChargeRate;

    public BigDecimal getHandlingChargeRate() {
        return handlingChargeRate;
    }

    public void setHandlingChargeRate(BigDecimal handlingChargeRate) {
        this.handlingChargeRate = handlingChargeRate;
    }
}
