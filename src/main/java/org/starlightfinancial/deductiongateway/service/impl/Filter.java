package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.utility.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/11/29
 */
public class Filter extends Decorator {

    private List deductionList = new ArrayList();

    @Autowired
    AccountManagerRepository accountManagerRepository;

    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        filter();
    }

    private void filter() {
        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        for (AutoBatchDeduction autoBatchDeduction : list) {
            AccountManager accountManager = accountManagerRepository.findByAccountAndSort(autoBatchDeduction.getAccout(), 1);
            if (null != accountManager && Constant.ENABLED_FALSE.equals(accountManager.getIsEnabled())) {
                list.remove(autoBatchDeduction);
            }

        }
        this.deductionList = list;
    }

    public List getDeductionList() {
        return deductionList;
    }
}
