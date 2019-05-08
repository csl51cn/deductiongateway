package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/20 15:28
 * @Modified By:
 */
public interface BusinessTransactionService {

    /**
     * 查询所有业务信息
     *
     * @return 所有业务信息
     */
    Map<String, BusinessTransaction> findAll();

}
