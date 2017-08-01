package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.SysUser;

/**
 * 系统服务Service接口
 */
public interface SystemService {
    SysUser findSysUser(String loginName, String password);
}
