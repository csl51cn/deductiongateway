package org.starlightfinancial.deductiongateway.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *页面跳转管理Controller
 */
@Controller
public class PageController {


    @RequestMapping("/")
    public  String   toLoginPage(){
        return "login";
    }


    @RequestMapping("/main.do")
    public  String   toMainPage(){
        return "main";
    }

    @RequestMapping("/deduction.do")
    public  String   toDeductionPage(){
        return "deduction";
    }


    @RequestMapping("/result.do")
    public  String   toResultPage(){
        return "result";
    }


}

