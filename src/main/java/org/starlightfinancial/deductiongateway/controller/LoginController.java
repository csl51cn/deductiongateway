package org.starlightfinancial.deductiongateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.starlightfinancial.deductiongateway.model.SysUser;
import org.starlightfinancial.deductiongateway.service.SystemService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 登录管理的Controller
 */
@Controller
public class LoginController {

    @Autowired
    private SystemService systemService;

    @RequestMapping("/login.do")
    public String login(String loginName, String password, HttpSession session, HttpServletRequest request) {
        SysUser loginUser = systemService.findSysUser(loginName, password);
        if (loginUser != null && loginUser.getId() > 0) {
            session.setAttribute("user", loginUser);
            String loginIP = request.getRemoteAddr();
           // systemService.createSysLoginHistory(loginUser.getId(), loginIP, "登录");
            return "main";
        }
        // TODO: 2017-07-21  登录失败后跳转页面 
        return "main";
    }
    
    @RequestMapping("/logout.do")
    public  String  logout(){
        // TODO: 2017-07-25  登出功能 
        return  "login";
    }
    
    
}


