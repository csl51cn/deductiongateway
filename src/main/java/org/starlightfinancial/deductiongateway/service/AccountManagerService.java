package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.List;

/**
 * 卡号管理Service接口
 *
 * @author senlin.deng
 */
public interface AccountManagerService {

    /**
     * 按条件分页查询记录
     *
     * @param contractNo  合同编号
     * @param bizNo       业务编号
     * @param accountName 账户名
     * @param pageBean    分页对象
     * @return
     */
    PageBean queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean);

    /**
     * 更新记录
     *
     * @param accountManager 代扣卡对象
     */
    void updateAccount(AccountManager accountManager);

    /**
     * 查找最后一条记录
     *
     * @return
     */
    List findLastAccount();

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     * @return
     */
    Message addAccount(String bizNo);


}
