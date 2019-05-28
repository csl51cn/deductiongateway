package org.starlightfinancial.deductiongateway.utility;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author: Senlin.Deng
 * @Description: Spring Context 工具类
 * @date: Created in 2018/7/20 15:38
 * @Modified By:
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取Bean对象
     *
     * @param name the name of the bean  to retrieve
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException 如果目标对象不能被创建,抛出异常
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    /**
     * 通过class获取Bean.
     *
     * @param clazz 目标类的Class对象
     * @param <T>   目标类类型
     * @return 返回目标类实例
     * @throws BeansException 如果目标对象不能被创建,抛出异常
     */
    public static <T> T getBean(Class<T> clazz) throws BeansException {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 获取当前环境
     * @return
     */
    public static String getActiveProfile() {
        return applicationContext.getEnvironment().getActiveProfiles()[0];
    }
}
