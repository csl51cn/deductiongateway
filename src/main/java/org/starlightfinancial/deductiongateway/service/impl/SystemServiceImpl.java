package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.dao.SysAutoNumDao;
import org.starlightfinancial.deductiongateway.model.SysAutoNum;
import org.starlightfinancial.deductiongateway.service.SystemService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SysAutoNumDao sysAutoNumDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public SysAutoNum loadSysAutoNum(int orgId, String type) {
        List<SysAutoNum> list = sysAutoNumDao.query(" and t.sysOrg=" + orgId
                + " and t.type = '" + type + "'");

        if (list != null && list.size() > 0)// 修改
        {
            return list.get(0);
        }
        return null;
    }

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
