package org.starlightfinancial.deductiongateway.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面跳转管理Controller
 */
@Controller
public class PageController {


    @RequestMapping("/")
    public String toIndexPage() {
        return "index";
    }

    @RequestMapping("/login")
    public String toLoginPage() {
        return "login";
    }


    @RequestMapping("/main.do")
    public String toMainPage() {
        return "main";
    }

    @RequestMapping("/deduction.do")
    public String toDeductionPage() {
        return "deduction";
    }


    @RequestMapping("/result.do")
    public String toResultPage() {
        return "result";
    }

    @RequestMapping("/accountmanager.do")
    public String toAccountManagerPage() {
        return "account-manager";
    }

    @RequestMapping("/limitmanager.do")
    public String toLimitManagerPage() {
        return "limit-manager";
    }

    @RequestMapping("/unionpay.do")
    public String toUnionPayPage() {
        return "union-pay";
    }

    @RequestMapping("/extraprocessing.do")
    public String toExtraProcessingPage() {
        return "extra-processing";
    }

    @RequestMapping("/deductiontemplate.do")
    public String toDeductionTemplatePage() {
        return "deduction-template";
    }

}

