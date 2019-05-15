package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by senlin.deng on 2017-08-28.
 */
public interface LimitManagerRepository extends JpaRepository<LimitManager, Integer>, JpaSpecificationExecutor<LimitManager> {

    /**
     * 通过银行编码,启用状态,代扣渠道查询限额记录
     *
     * @param bankCode 银行编码
     * @param enabled  启用状态
     * @param channel  代扣渠道
     * @return 限额记录
     */
    LimitManager findByBankCodeAndEnabledAndChannel(String bankCode, String enabled, String channel);

    /**
     * 通过银行编码,启用状态查询限额记录
     *
     * @param bankCode 银行编码
     * @param enabled  启用状态
     * @return 限额记录
     */
    List<LimitManager> findByBankCodeAndEnabled(String bankCode, String enabled);

    /**
     * 通过银行编码,渠道查询限额记录
     * @param bankCode 银行编码
     * @param channel 渠道
     * @return 查询到的记录
     */
    LimitManager  findByBankCodeAndChannel(String bankCode,String channel);
}