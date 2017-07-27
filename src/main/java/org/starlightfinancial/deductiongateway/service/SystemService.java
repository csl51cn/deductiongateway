package org.starlightfinancial.deductiongateway.service;


import org.starlightfinancial.deductiongateway.model.SysAutoNum;
import org.starlightfinancial.deductiongateway.model.SysLoginHistory;
import org.starlightfinancial.deductiongateway.model.SysUser;

public interface SystemService extends BaseService {

    public SysAutoNum loadSysAutoNum(int orgId, String type);


    SysUser findSysUser(String username, String password);

    SysLoginHistory createSysLoginHistory(int user_id, String loginIP, String remark);
}
