package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.vo.AccountManagerVO;

import java.util.Map;

/**
 * 卡号管理Service接口
 *
 * @author senlin.deng
 */
public interface AccountManagerService {

    /**
     * 按条件分页查询记录
     *
     * @param accountManagerVO 查询条件
     * @param pageBean         分页对象
     * @return
     */
    PageBean queryAccount(AccountManagerVO accountManagerVO, PageBean pageBean);

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
    AccountManager findLastAccount();

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     * @return
     */
    Message addAccount(String bizNo);

    /**
     * 根据id查询卡信息
     *
     * @param id
     * @return
     */
    AccountManager findById(Integer id);

    /**
     * 按条件查询卡号记录,并生成白明单,map是文件名与白名单内容的映射
     * @param accountManagerVO
     * @return
     */
    Map<String, String> whiteListExport(AccountManagerVO accountManagerVO);
}
