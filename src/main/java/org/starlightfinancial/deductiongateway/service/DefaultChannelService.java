package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.DefaultChannel;
import org.starlightfinancial.deductiongateway.utility.PageBean;

/**
 * @author: Senlin.Deng
 * @Description: 默认渠道Service
 * @date: Created in 2019/7/10 10:38
 * @Modified By:
 */
public interface DefaultChannelService {

    /**
     * 通过银行编码查询记录是否存在
     *
     * @param bankCode 银行编码
     * @return
     */
    boolean isExisted(String bankCode);

    /**
     * 保存记录
     *
     * @param defaultChannel 默认渠道
     */
    void saveDefaultChannel(DefaultChannel defaultChannel);

    /**
     * 条件查询记录
     * @param pageBean 分页参数
     * @param defaultChannel  包含查询条件
     * @return
     */
    PageBean queryDefaultChannel(PageBean pageBean, DefaultChannel defaultChannel);

    /**
     * 更新记录
     * @param defaultChannel
     */
    void updateDefaultChannel(DefaultChannel defaultChannel);


    /**
     * 通过银行编码查询记录
     * @param bankCode
     * @return
     */
    DefaultChannel getByBankCode(String bankCode);
}
