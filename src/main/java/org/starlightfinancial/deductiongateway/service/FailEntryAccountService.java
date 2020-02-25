package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.FailEntryAccount;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/23 13:02
 * @Modified By:
 */
public interface FailEntryAccountService {

    /**
     * 保存不能足额入账的信息
     * @param failEntryAccount
     */
    void save(FailEntryAccount failEntryAccount);

    /**
     * 通过条件查询记录
     * @param contractNo 合同号
     * @param date 生成日期
     * @return
     */
    FailEntryAccount findByContractNoAndCreateDate(String contractNo, Date date);
}
