package org.starlightfinancial.deductiongateway.service;


import org.starlightfinancial.deductiongateway.model.SysAutoNum;

public interface SystemService extends BaseService {

    public SysAutoNum loadSysAutoNum(int orgId, String type);

}
