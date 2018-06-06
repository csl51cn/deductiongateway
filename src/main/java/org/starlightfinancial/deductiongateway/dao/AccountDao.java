package org.starlightfinancial.deductiongateway.dao;

import org.starlightfinancial.deductiongateway.domain.local.AccountManager;

import java.util.List;

/**
 * @author: senlin.deng
 * @Description: 代扣卡信息管理Dao
 * @date: Created in 2018/5/7 13:36
 * @Modified By:
 */
public interface AccountDao {


    /**
     * 通过业务编号查询date_id
     * @param bizNo 业务编号
     * @return
     */
    Integer findDateIdByBizNo(String bizNo);

    /**
     * 根据dateId查询代扣卡信息
     * @param dateId
     * @return
     */
    List<AccountManager> findAccountByDateId(Integer dateId);
}
