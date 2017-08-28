package org.starlightfinancial.deductiongateway.service;

/**
 * Created by sili.chen on 2017/8/23
 */
public class ConcreteComponent implements Route {
    @Override
    public void doRoute() {
        System.out.println("ConcreteComponet is woring");
    }
}
