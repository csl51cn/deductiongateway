package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.service.AssemblerFactory;
import org.starlightfinancial.deductiongateway.service.Delivery;
import org.starlightfinancial.deductiongateway.service.Handler;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.util.List;


/**
 * Created by sili.chen on 2017/8/23
 */
public class ConcreteHandler extends Handler implements ItemProcessor {

    @Autowired
    Splitter splitter;

    @Autowired
    Filter filter;

    @Autowired
    AssemblerFactory assemblerFactory;

    @Autowired
    MetadataValidator metadataValidator;

    @Autowired
    Delivery delivery;

    @Autowired
    HttpClientUtil httpClientUtil;

    private AutoBatchDeduction autoBatchDeduction;

    @Override
    public List<MortgageDeduction> handleRequest() {
        try {
            Assembler assembler = assemblerFactory.getAssembleImpl("auto");
            splitter.setRoute(metadataValidator);
            filter.setRoute(splitter);
            assembler.setRoute(filter);
            delivery.setRoute(assembler);
            delivery.doRoute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return delivery.getResult();
    }

    @Override
    public Object process(Object o) {
        this.autoBatchDeduction = (AutoBatchDeduction) o;
        metadataValidator.setAutoBatchDeduction(autoBatchDeduction);
        splitter.setAutoBatchDeduction(autoBatchDeduction);
        return this.handleRequest();
    }
}
