package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

/**
 * 限额管理Service接口
 */
public interface LimitManagerService {

    PageBean queryAllLimit(PageBean pageBean);

    void saveOrUpdateLimit(LimitManager limitManager);
}
