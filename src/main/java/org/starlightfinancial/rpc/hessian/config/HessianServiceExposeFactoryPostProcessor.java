package org.starlightfinancial.rpc.hessian.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.remoting.caucho.HessianServiceExporter;
import org.springframework.stereotype.Component;
import org.starlightfinancial.rpc.hessian.annotation.HessianService;

/**
 * @author: Senlin.Deng
 * @Description: 暴露Hessian服务接口
 * @date: Created in 2019/4/15 16:21
 * @Modified By:
 */
@Component
public class HessianServiceExposeFactoryPostProcessor implements BeanFactoryPostProcessor {


    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        String[] beanNames = beanFactory.getBeanNamesForAnnotation(HessianService.class);
        for (String beanName : beanNames) {
            String className = beanFactory.getBeanDefinition(beanName).getBeanClassName();
            Class<?> clasz = null;
            try {
                clasz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new BeanInitializationException(e.getMessage(), e);
            }
            String hessianServiceBeanName = "/cpcn/" + beanName.replace("Impl", "");

            BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(HessianServiceExporter.class);
            builder.addPropertyReference("service", beanName);
            builder.addPropertyValue("serviceInterface",clasz.getInterfaces()[0].getName());
            ((BeanDefinitionRegistry) beanFactory).registerBeanDefinition(hessianServiceBeanName, builder.getBeanDefinition());
        }
//        String[] beans = beanFactory.getBeanDefinitionNames();
//        for (String beanDefinitionName : beans) {
//            Class<?> beanType = beanFactory.getType(beanDefinitionName);
//            HessianService hessianService = AnnotationUtils.findAnnotation(beanType, HessianService.class);
//            if (hessianService != null) {
//                String hessianServiceBeanName = "/cpcn/" + beanDefinitionName.replace("Impl", "");
//                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(HessianServiceExporter.class);
//                 builder.addPropertyReference("service", beanName);
//                builder.addPropertyValue("serviceInterface", clasz.getSuperclass().getInterfaces()[0].getName());
//
//                beanFactory.registerSingleton(name, newWS);
//            }
//
//
//        }


    }
}
