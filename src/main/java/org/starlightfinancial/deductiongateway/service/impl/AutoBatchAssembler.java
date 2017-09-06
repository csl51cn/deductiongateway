package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AutoBatchAssembler extends Assembler {

    @Autowired
    private HttpClientUtil httpClientUtil;

    @Override
    public void assembleMessage() {
        List<GoPayBean> messages = new ArrayList<>();
        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        for (AutoBatchDeduction autoBatchDeduction : list) {
            GoPayBean goPayBean = autoBatchDeduction.transToGoPayBean();
            messages.add(goPayBean);
        }
        httpClientUtil.sendInformation(messages);
    }
}
