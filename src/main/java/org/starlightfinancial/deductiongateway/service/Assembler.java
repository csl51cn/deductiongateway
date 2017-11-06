package org.starlightfinancial.deductiongateway.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/8/23
 */
public abstract class Assembler extends Decorator {

    private List result;

    public abstract void assembleMessage() throws Exception;

    @Override
    public void doRoute() throws Exception {
        result = new ArrayList();
        super.doRoute();
        this.assembleMessage();
    }

    public List getResult() {
        return result;
    }

    public void setResult(List result) {
        this.result = result;
    }
}
