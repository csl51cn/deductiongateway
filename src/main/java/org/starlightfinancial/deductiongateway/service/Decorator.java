package org.starlightfinancial.deductiongateway.service;

/**
 * 装饰器
 * Created by sili.chen on 2017/8/23
 */
public abstract class Decorator implements Route {
    protected Route route;

    @Override
    public void doRoute() {
        if (null != route) {
            route.doRoute();
        }
    }

    public void setRoute(Route route) {
        this.route = route;
    }
}
