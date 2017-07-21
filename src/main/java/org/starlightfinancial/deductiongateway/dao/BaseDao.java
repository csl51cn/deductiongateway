package org.starlightfinancial.deductiongateway.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BaseDao<T> {
    public T load(Integer id);

    public T save(T obj);

    public T update(T obj);

    public T saveOrUpdate(T obj);

    public boolean delete(T obj);

    public boolean delete(Integer id);

    public List<T> query(String condition);

    /**
     * 从筛选的结果里取出第一个
     *
     * @author piggy
     */
    <T2> T2 queryForObject(String hql, Class<T2> requiredType);

    /**
     * 输入SQL，返回集合
     *
     * @author piggy
     */
    <T3> List<T3> queryForList(String sql, Class<T3> requiredType);

    public List<T> pageQuery(String condition, int start, int length);

    public int count(String condition);

    public T refresh(T obj);

    public void clearSession();

    @SuppressWarnings("rawtypes")
    public List<Map> executeSQL(String condition);

    void executeUpdate(String sql);

    Map<String, Object> queryForMap(String sql);

    int queryForInt(String sql);

    BigDecimal queryForBigDecimal(String sql);
}
