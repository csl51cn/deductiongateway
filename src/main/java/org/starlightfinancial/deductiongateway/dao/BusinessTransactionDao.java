package org.starlightfinancial.deductiongateway.dao;

import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/20 17:19
 * @Modified By:
 */
public interface BusinessTransactionDao {

    /**
     * 查询所有业务信息
     * @return 所有业务信息
     */
    List<Map<String,Object>> findAll();
}
