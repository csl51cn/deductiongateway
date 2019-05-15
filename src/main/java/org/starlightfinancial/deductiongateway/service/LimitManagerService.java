package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

/**
 * 限额管理Service接口
 */
public interface LimitManagerService {

    PageBean queryLimit(PageBean pageBean, LimitManager limitManager);

    void saveOrUpdateLimit(LimitManager limitManager);

    /**
     * 通过银行编码和渠道查询是否存在记录
     * @param bankCode  银行编码
     * @param channel 渠道
     * @return 结果
     */
    boolean isExisted(String bankCode, String channel);
}
