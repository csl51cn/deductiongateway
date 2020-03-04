package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.dto.SurplusTotalAmount;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 多期逾期管理Service接口
 * @date: Created in 2020/2/20 14:56
 * @Modified By:
 */
public interface MultiOverdueService {
    /**
     * 计算剩余应还金额
     *
     * @param accountManager 卡号信息
     * @return
     */
    SurplusTotalAmount obtainSurplusTotalAmount(AccountManager accountManager);


    /**
     * 获取豁免信息
     *
     * @param accountManager         卡号信息
     * @param repaymentAmount 已还金额
     * @param applyDate              豁免申请还款时间
     * @param repaymentType          还款类别
     * @return
     */
    SurplusTotalAmount obtainExemptInfo(AccountManager accountManager, BigDecimal repaymentAmount, Date applyDate, Integer repaymentType);
}
