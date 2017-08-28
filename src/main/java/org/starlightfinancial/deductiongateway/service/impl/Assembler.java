package org.starlightfinancial.deductiongateway.service.impl;

import org.starlightfinancial.deductiongateway.service.Asssemble;
import org.starlightfinancial.deductiongateway.service.Decorator;

/**
 * Created by sili.chen on 2017/8/23
 */
public class Assembler extends Decorator implements Asssemble {
    @Override
    public void assembleMessage() {

    }

    @Override
    public void doRoute() {
        super.doRoute();
        this.assembleMessage();
        System.out.println("Assembler is working");
    }
}
