package org.starlightfinancial.deductiongateway.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.common.SessionManager;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;
import org.starlightfinancial.deductiongateway.service.SystemService;

import javax.servlet.http.HttpSession;

/**
 * 登录管理的Controller
 */
@Controller
public class LoginController {

    @Autowired
    private SystemService systemService;

    @RequestMapping("/login.do")
    public String login(String loginName, String password, HttpSession session) {
        if (StringUtils.isEmpty(loginName) || StringUtils.isEmpty(password)) {
            session.setAttribute("msg", "登录失败,用户名或密码为空");
            return "login";
        }

        SysUser loginUser = systemService.findSysUser(loginName, password);
        if (loginUser != null && loginUser.getId() > 0) {
//            session.setAttribute("loginUser", loginUser);
            SessionManager.userLoginHandle(loginUser, session);
            return "main";
        }
        session.setAttribute("msg", "登录失败,用户名或密码错误/账户不可用");
        return "login";
    }

    @RequestMapping("/logout.do")
    public String logout(HttpSession session) {
        SessionManager.destroySession(session);
//        session.removeAttribute("loginUser");
//        session.removeAttribute("msg");
        return "login";
    }

    /**
     * 修改密码
     *
     * @param newPassword
     * @param session
     * @return
     */
    @RequestMapping("/resetPassword.do")
    @ResponseBody
    public Message resetPassword(String newPassword,HttpSession session) {
        try {
            systemService.resetPassword(newPassword, (SysUser) session.getAttribute("loginUser"));
            return Message.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail();
        }
    }


}


