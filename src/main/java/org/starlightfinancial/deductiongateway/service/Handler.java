package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.util.List;

/**
 * Created by sili.chen on 2017/8/23
 */
public abstract class Handler {
    protected Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract List<MortgageDeduction> handleRequest();
}
