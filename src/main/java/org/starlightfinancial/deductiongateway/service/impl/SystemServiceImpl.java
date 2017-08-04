package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.MD5Value;
import org.starlightfinancial.deductiongateway.domain.MD5ValueRepository;
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

    @Autowired
    MD5ValueRepository md5ValueRepository;

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
        System.out.println("Password" + de_password);
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

    /**
     * 查询所有md5值
     * @return
     */
    @Override
    public List<MD5Value> findAllMD5() {
        List<MD5Value> list = md5ValueRepository.findAll();
        return list;
    }

    /**
     * 保存md5值
     * @param md5Valeu
     */
    @Override
    public void saveMD5(String md5Valeu) {
        MD5Value md5Value = new MD5Value();
        md5Value.setMd5(md5Valeu);
        md5ValueRepository.save(md5Value);
    }
}
