package org.starlightfinancial.deductiongateway.faces;

import org.starlightfinancial.deductiongateway.service.impl.ConcreteHandler;

/**
 * Created by sili.chen on 2017/8/23
 */
public class Test {
    public static void main(String[] args) {
        ConcreteHandler concreteHandler = new ConcreteHandler();
        concreteHandler.setSuccessor(concreteHandler);
//        concreteHandler.handleRequest();


    }
}
