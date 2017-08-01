package org.starlightfinancial.deductiongateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.starlightfinancial.deductiongateway.domain.SysUser;
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
        SysUser loginUser = systemService.findSysUser(loginName, password);
        if (loginUser != null && loginUser.getId() > 0) {
            session.setAttribute("loginUser", loginUser);
            return "main";
        }
        session.setAttribute("msg","登录失败,用户名或密码错误/账户不可用");
        return "login";
    }

    @RequestMapping("/logout.do")
    public  String  logout(HttpSession session){
        session.removeAttribute("loginUser");
        session.removeAttribute("msg");
        return  "login";
    }
    
    
}


