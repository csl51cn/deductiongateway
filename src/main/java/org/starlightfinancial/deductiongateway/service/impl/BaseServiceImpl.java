package org.starlightfinancial.deductiongateway.service.impl;

import org.starlightfinancial.deductiongateway.service.BaseService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements BaseService {


    public List<Map> executeSQL(String condition) {
        return null;
    }

    public <T> T queryForObject(String condition, Class<T> requiredType) {
        return null;
    }

    public <T3> List<T3> queryForList(String sql, Class<T3> requiredType) {
        return null;
    }

    public Map queryForMap(String sql) {
        return null;
    }

    public int queryForInt(String sql) {
        return 0;
    }

    public BigDecimal queryForBigDecimal(String sql) {
        return null;
    }
}