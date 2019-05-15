package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.MD5Value;
import org.starlightfinancial.deductiongateway.domain.local.SysDict;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import java.util.List;

/**
 * 系统服务Service接口
 */
public interface SystemService {


    /**
     * 查找用户
     *
     * @param loginName 登录名
     * @param password  密码
     * @return
     */
    SysUser findSysUser(String loginName, String password);

    /**
     * 获取所欲md5值
     *
     * @return
     */
    List<MD5Value> findAllMD5();

    /**
     * 保存文件的md5值
     *
     * @param md5Value
     */
    void saveMD5(String md5Value);

    /**
     * 更新密码
     *
     * @param password 新密码
     * @param user     用户
     */
    void resetPassword(String password, SysUser user);

    /**
     * 根据字典类型查找字典值
     *
     * @param dicType 字典类型
     * @return
     */
    List<SysDict> findByDicType(String dicType);
}
