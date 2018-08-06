package org.starlightfinancial.deductiongateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.starlightfinancial.deductiongateway.common.LoginInterceptor;
import org.starlightfinancial.deductiongateway.common.SameUrlDataInterceptor;

/**
 * Created by sili.chen on 2017/7/31
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurerAdapter {
    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SameUrlDataInterceptor()).addPathPatterns("/**");
    }

    /**
     * 页面跳转
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/main.do").setViewName("main");
        registry.addViewController("/deduction.do").setViewName("deduction");
        registry.addViewController("/result.do").setViewName("result");
        registry.addViewController("/accountmanager.do").setViewName("account-manager");
        registry.addViewController("/limitmanager.do").setViewName("limit-manager");
        registry.addViewController("/unionpay.do").setViewName("union-pay");
        registry.addViewController("/extraprocessing.do").setViewName("extra-processing");
        registry.addViewController("/deductiontemplate.do").setViewName("deduction-template");
        registry.addViewController("/reset-password").setViewName("reset-password");
        registry.addViewController("/non-deduction-upload.do").setViewName("non-deduction-repayment-info-upload");
        registry.addViewController("/non-deduction-show.do").setViewName("non-deduction-repayment-info-show");
        registry.addViewController("/associate-payer.do").setViewName("associate-payer");
    }
}

