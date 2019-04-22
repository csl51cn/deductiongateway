package org.starlightfinancial.deductiongateway.utility;

import com.caucho.hessian.client.HessianProxyFactory;

/**
 * @author: Senlin.Deng
 * @Description: HessianProxyFactory 工具类
 * @date: Created in 2019/4/15 15:06
 * @Modified By:
 */
public class HessianProxyFactoryUtils {


    /**
     * 获取调用端对象
     *
     * @param clazz 实体对象类型
     * @param url   客户端url地址
     * @param <T>   泛型
     * @return 业务对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T getHessianClientBean(Class<T> clazz, String url) throws Exception {
        // 客户端连接工厂,这里只是做了最简单的实例化，还可以设置超时时间，密码等安全参数
        HessianProxyFactory factory = new HessianProxyFactory();
        factory.setOverloadEnabled(true);
        return (T) factory.create(clazz, url);
    }


}
