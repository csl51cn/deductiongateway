package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.SysDictRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AutoBatchAssembler extends Assembler {
    @Autowired
    SysDictRepository sysDictRepository;

    @Value("${batch.route.use}")
    private String router;

    @Autowired
    BaofuConfig baofuConfig;

    @Autowired
    ChinaPayConfig chinaPayConfig;

    @Autowired
    BeanConverter beanConverter;

    @Override
    public void assembleMessage() throws Exception {
        List<AutoBatchDeduction> list = ((Filter) this.route).getDeductionList();
        if ("UNIONPAY".equals(router)) {
            assembleUNIONPAY(list);
        } else if ("BAOFU".equals(router)) {
            assembleBAOFU(list);
        }
    }

    private void assembleUNIONPAY(List<AutoBatchDeduction> list) throws Exception {
        for (AutoBatchDeduction autoBatchDeduction : list) {
            MortgageDeduction mortgageDeduction = beanConverter.transToMortgageDeduction(autoBatchDeduction);
            getResult().add(mortgageDeduction);
        }

    }

    private void assembleBAOFU(List<AutoBatchDeduction> list) throws UnsupportedEncodingException {
        for (AutoBatchDeduction autoBatchDeduction : list) {
            MortgageDeduction mortgageDeduction = beanConverter.transToMortgageDeduction(autoBatchDeduction);
            getResult().add(mortgageDeduction);
        }
    }

}
