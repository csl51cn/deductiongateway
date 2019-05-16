package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;

/**
 * 限额管理Service接口
 */
public interface LimitManagerService {

    /**
     * 根据条件查询限额记录
     *
     * @param pageBean     分页参数
     * @param limitManager 查询条件
     * @return
     */
    PageBean queryLimit(PageBean pageBean, LimitManager limitManager);

    /**
     * 更新
     *
     * @param limitManager 限额记录
     */
    void saveLimit(LimitManager limitManager);

    /**
     * 保存记录
     *
     * @param limitManager 限额记录
     */
    void updateLimit(LimitManager limitManager);

    /**
     * 通过银行编码和渠道查询是否存在记录
     *
     * @param bankCode 银行编码
     * @param channel  渠道
     * @return 结果
     */
    boolean isExisted(String bankCode, String channel);


    /**
     * 修改启用状态
     *
     * @param ids     一个或多个记录的id
     */
    void modifyEnabledStatus(String ids);
}
