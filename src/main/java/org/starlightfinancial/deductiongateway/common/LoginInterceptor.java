package org.starlightfinancial.deductiongateway.common;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by senlin.deng on 2017-08-02.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("/login.do".equals(request.getRequestURI()) || "/".equals(request.getRequestURI()) || "/login".equals(request.getRequestURI())
                || "/BaoFuPayGetBgAsyn".equals(request.getRequestURI()) || "/PayGetBgAsyn".equals(request.getRequestURI())
                || "/PayGetPgAsyn".equals(request.getRequestURI()) || "/unionPayController/bgNotification".equals(request.getRequestURI())) {
            return true;
        }

        SysUser loginUser = (SysUser) request.getSession().getAttribute("loginUser");
        if (loginUser == null) {
            response.sendRedirect("/login");
            return false;
        }
        if (!UserContext.hasUser()){
            UserContext.setUser(loginUser);
        }

        return true;
    }


    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //清除线程数据
        UserContext.clear();
    }

}
