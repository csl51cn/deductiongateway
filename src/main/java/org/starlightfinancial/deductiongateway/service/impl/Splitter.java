package org.starlightfinancial.deductiongateway.service.impl;

import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.service.SplitAccount;

/**
 * Created by sili.chen on 2017/8/23
 */
public class Splitter extends Decorator implements SplitAccount {
    @Override
    public void doSplitAccount() {

    }

    @Override
    public void doRoute() {
        super.doRoute();
        this.doSplitAccount();
        System.out.println("splitter is working");
    }
}
