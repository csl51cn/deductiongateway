package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.MD5Value;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import java.util.List;

/**
 * 系统服务Service接口
 */
public interface SystemService {
    SysUser findSysUser(String loginName, String password);

    List<MD5Value> findAllMD5();

    void saveMD5(String md5Value);
}
