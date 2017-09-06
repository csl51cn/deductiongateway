package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

/**
 * 卡号管理Service接口
 */
public interface AccountManagerService {
    PageBean queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean);

    void updateAccount(AccountManager accountManager);
}
