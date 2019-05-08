package org.starlightfinancial.deductiongateway.common;

import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/30 13:31
 * @Modified By:
 */
public class UserContext {
    public static final ThreadLocal<SysUser> threadUser = new ThreadLocal<>();

    /**
     * 获取当前用户
     * @return
     */
    public static SysUser getUser(){
        return   threadUser.get();
    }

    /**
     * 设置当前登录用户
     * @param sysUser
     */
    public static void setUser(SysUser sysUser){
        threadUser.set(sysUser);
    }

    /**
     * 当前线程是否有登录用户
     * @return 存在登录用户返回true,否则返回false
     */
    public static boolean   hasUser(){
        return !Objects.isNull(getUser());
    }

    /**
     * 清除当前线程变量
     */
    public static void clear(){
        threadUser.remove();
    }
}
