package org.starlightfinancial.deductiongateway.service;

/**
 * Created by sili.chen on 2017/8/23
 */
public abstract class Handler {
    protected Handler successor;

    public void setSuccessor(Handler successor) {
        this.successor = successor;
    }

    public abstract void handleRequest();
}
