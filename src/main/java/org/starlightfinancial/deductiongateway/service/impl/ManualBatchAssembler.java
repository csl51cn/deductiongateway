package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class ManualBatchAssembler extends Assembler {

    @Autowired
    MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    HttpClientUtil httpClientUtil;

    @Autowired
    ChinaPayConfig chinaPayConfig;


    @Autowired
    BaofuConfig baofuConfig;

    @Autowired
    BeanConverter beanConverter;

    @Override
    public void assembleMessage() throws Exception {

    }


}
