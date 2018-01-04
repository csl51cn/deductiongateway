package org.starlightfinancial.deductiongateway.service;

/**
 * Created by sili.chen on 2018/1/3
 */
public class Delivery extends Decorator {

    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        delivery();
    }

    private void send() {

    }
}
