package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.ItemProcessor;
import org.starlightfinancial.deductiongateway.service.ConcreteComponent;
import org.starlightfinancial.deductiongateway.service.Handler;


/**
 * Created by sili.chen on 2017/8/23
 */
public class ConcreteHandler extends Handler implements ItemProcessor {


    @Override
    public void handleRequest(Object o) {
        ConcreteComponent concreteComponent = new ConcreteComponent();
        MetadataValidator metadataValidator = new MetadataValidator(o);
        Splitter splitter = new Splitter();
        Assembler assembler = new Assembler();

        metadataValidator.setRoute(concreteComponent);
        splitter.setRoute(metadataValidator);
        assembler.setRoute(splitter);

        assembler.doRoute();

        boolean result = false;
        if (result) {
            System.out.println("to do something");
        } else {
            //successor.handleRequest();
        }
    }

    @Override
    public Object process(Object o) throws Exception {
        System.out.println(o);
        this.handleRequest(o);
        return null;
    }
}
