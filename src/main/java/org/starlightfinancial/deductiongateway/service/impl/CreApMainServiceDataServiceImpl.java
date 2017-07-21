package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.dao.CreApMainServiceDao;
import org.starlightfinancial.deductiongateway.model.CreApMainServiceData;
import org.starlightfinancial.deductiongateway.service.CreApMainServiceDataService;

public class CreApMainServiceDataServiceImpl implements CreApMainServiceDataService {
    @Autowired
    private CreApMainServiceDao creApMainServiceDao;

    public CreApMainServiceData getCreApMainServiceData(int id) {
        return creApMainServiceDao.load(id);
    }
}
