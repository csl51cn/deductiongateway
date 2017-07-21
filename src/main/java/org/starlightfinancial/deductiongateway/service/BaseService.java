package org.starlightfinancial.deductiongateway.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface BaseService {
    public List<Map> executeSQL(String condition);

    /**
     * 返回记录集的第一条，实体类
     *
     * @author piggy
     */
    <T> T queryForObject(String condition, Class<T> requiredType);

    /**
     * 输入SQL，返回集合
     *
     * @author piggy
     */
    <T3> List<T3> queryForList(String sql, Class<T3> requiredType);

    Map queryForMap(String sql);

    int queryForInt(String sql);

    BigDecimal queryForBigDecimal(String sql);
}