package org.starlightfinancial.deductiongateway.dto;

import lombok.Data;
import org.starlightfinancial.deductiongateway.domain.remote.ExemptInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 剩余还款金额
 * @date: Created in 2020/2/20 10:58
 * @Modified By:
 */
@Data
public class SurplusTotalAmount {
    /**
     * 未还本息,可能包含罚息
     */
    private BigDecimal principalAndInterest;


    /**
     * 未还服务费,可能包含罚息
     */
    private BigDecimal serviceFee;

    /**
     * 是否逾期:true-是,false-否
     */
    private Boolean overdueFlag;

    /**
     * 豁免信息
     */
    private List<ExemptInfo> exemptInfos;


    /**
     * 标志能否入账:true-可以,false-不可以
     */
    private Boolean clearFlag;


}
