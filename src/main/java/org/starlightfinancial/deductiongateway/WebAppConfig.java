package org.starlightfinancial.deductiongateway;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
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
     * @author lance
     */
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(new SameUrlDataInterceptor()).addPathPatterns("/**");
    }
}

