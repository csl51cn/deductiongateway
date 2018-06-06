package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.utility.Constant;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sili.chen on 2017/11/29
 */
@Component
public class Filter extends Decorator {

    private List<AutoBatchDeduction> deductionList;

    @Autowired
    AccountManagerRepository accountManagerRepository;

    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        filter();
    }

    private void filter() {
        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        Iterator<AutoBatchDeduction> iterator = list.iterator();
        while (iterator.hasNext()) {
            AutoBatchDeduction autoBatchDeduction = iterator.next();
            AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(autoBatchDeduction.getAccout(),
                    1, autoBatchDeduction.getContractNo());
            if (null != accountManager && Constant.ENABLED_FALSE.equals(accountManager.getIsEnabled())) {
                iterator.remove();
            }
        }
        this.deductionList = list;
    }

    public List<AutoBatchDeduction> getDeductionList() {
        return this.deductionList;
    }
}
