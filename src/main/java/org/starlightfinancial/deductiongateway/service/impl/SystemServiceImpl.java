package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.SysUser;
import org.starlightfinancial.deductiongateway.domain.SysUserRepository;
import org.starlightfinancial.deductiongateway.service.SystemService;
import org.starlightfinancial.deductiongateway.utility.EncryptHelper;

import java.util.List;

/**
 * 系统服务管理Service
 */
@Service
public class SystemServiceImpl implements SystemService {
    @Autowired
    private SysUserRepository sysUserRepository;


    /**
     * 根据登录名和密码查询用户
     *
     * @param loginName
     * @param password
     * @return
     */
    @Override
    public SysUser findSysUser(String loginName, String password) {

        String de_password = EncryptHelper.Instance.getEncString(password);
        if (StringUtils.isEmpty(de_password))
            return null;
        de_password = de_password.trim();
        List<SysUser> listSysUser = sysUserRepository.findByLoginNameAndLoginPasswordAndDeleteFlag(loginName, de_password, "0");
        if (listSysUser.size() > 0) {
            return listSysUser.get(0);
        } else {
            return null;
        }
    }
}
