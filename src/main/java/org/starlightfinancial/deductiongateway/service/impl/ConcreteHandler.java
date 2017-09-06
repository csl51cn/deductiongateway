package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.service.AssemblerFactory;
import org.starlightfinancial.deductiongateway.service.Handler;


/**
 * Created by sili.chen on 2017/8/23
 */
public class ConcreteHandler extends Handler implements ItemProcessor {

    @Autowired
    Splitter splitter;

    @Autowired
    AssemblerFactory assemblerFactory;

    @Autowired
    MetadataValidator metadataValidator;

    private AutoBatchDeduction autoBatchDeduction;

    @Override
    public void handleRequest() {
        try {
            Assembler assembler = assemblerFactory.getAssembleImpl("auto");
            splitter.setRoute(metadataValidator);
            assembler.setRoute(splitter);
            assembler.doRoute();

            boolean result = false;
            if (result) {
                System.out.println("to do something");
            } else {
                successor.handleRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object process(Object o) throws Exception {
        this.autoBatchDeduction = (AutoBatchDeduction) o;
        splitter.setAutoBatchDeduction(autoBatchDeduction);
        this.handleRequest();
        return o;
    }
}
