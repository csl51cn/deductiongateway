package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/8/23
 */
public abstract class Assembler extends Decorator {

    private List<GoPayBean> messages = new ArrayList<>();

    public abstract void assembleMessage();

    @Override
    public void doRoute() {
        super.doRoute();
        this.assembleMessage();
        System.out.println("Assembler is working");
    }
}
