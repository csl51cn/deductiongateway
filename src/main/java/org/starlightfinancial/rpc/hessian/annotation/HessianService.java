package org.starlightfinancial.rpc.hessian.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * @author: Senlin.Deng
 * @Description: 使用在Hessian服务接口实现类上, 为了自动对外暴露接口服务
 * @date: Created in 2019/4/15 15:58
 * @Modified By:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface HessianService {

    /**
     * 组件名
     */
    String value() default "";

}
