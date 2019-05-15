package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
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
    MetadataValidator metadataValidator;

    @Autowired
    Delivery delivery;

    @Autowired
    HttpClientUtil httpClientUtil;


    private AutoBatchDeduction autoBatchDeduction;


    @Override
    public List<MortgageDeduction> handleRequest() {
        try {
            //校验--过滤--拆分(包含转换)--代扣
            filter.setRoute(metadataValidator);
            splitter.setRoute(filter);
            delivery.setRoute(splitter);
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
        filter.setAutoBatchDeduction(autoBatchDeduction);
        return this.handleRequest();
    }
}
