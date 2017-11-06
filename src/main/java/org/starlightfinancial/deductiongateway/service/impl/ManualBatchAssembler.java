package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class ManualBatchAssembler extends Assembler {
    @Override
    public void assembleMessage() {
        List<GoPayBean> messages = new ArrayList<>();
        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        for (AutoBatchDeduction autoBatchDeduction : list) {

        }
    }
}
