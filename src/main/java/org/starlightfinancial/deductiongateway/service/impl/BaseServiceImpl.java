package org.starlightfinancial.deductiongateway.service.impl;

import forward.chuwa.microcredit.dao.CreApMainDao;
import forward.chuwa.microcredit.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BaseServiceImpl implements BaseService {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private CreApMainDao creApMainDao;

    @SuppressWarnings("rawtypes")
    public List<Map> executeSQL(String condition) {
        return creApMainDao.executeSQL(condition);
    }

    @SuppressWarnings("rawtypes")
    public Map queryForMap(String sql) {
        return creApMainDao.queryForMap(sql);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public int queryForInt(String sql) {
        return creApMainDao.queryForInt(sql);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public <T> T queryForObject(String condition, Class<T> requiredType) {
        return creApMainDao.queryForObject(condition, requiredType);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public <T> List<T> queryForList(String sql, Class<T> requiredType) {
        if (logger.isDebugEnabled()) {
            logger.debug("SQL: {}", sql);
        }
        return creApMainDao.queryForList(sql, requiredType);
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public BigDecimal queryForBigDecimal(String sql) {
        return creApMainDao.queryForBigDecimal(sql);
    }
}