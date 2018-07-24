package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.AssociatePayer;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 关联还款人Service 接口
 * @date: Created in 2018/7/23 11:45
 * @Modified By:
 */
public interface AssociatePayerService {

    /**
     * 查找所有记录
     *
     * @return 关联还款人信息
     */
    List<AssociatePayer> findAll();

    /**
     * 根据条件查询记录
     *
     * @param pageBean   页面参数对象
     * @param contractNo 合同号
     * @return 查询出来的记录
     */
    PageBean queryAssociatePayer(PageBean pageBean, String contractNo);

    /**
     * 保存记录
     *
     * @param associatePayer 关联还款人
     */
    void saveAssociatePayer(AssociatePayer associatePayer);

    /**
     * 更新记录
     *
     * @param associatePayer 关联还款人
     */
    void updateAssociatePayer(AssociatePayer associatePayer);
}
