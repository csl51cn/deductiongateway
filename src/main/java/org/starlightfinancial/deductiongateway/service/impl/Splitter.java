package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by sili.chen on 2017/8/23
 */
@Component
public class Splitter extends Decorator {

    @Autowired
    private ChannelDispatchService channelDispatchService;
    @Autowired
    private BeanConverter converter;

    private List<MortgageDeduction> deductionList;

    public List getDeductionList() {
        return deductionList;
    }


    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        AutoBatchDeduction autoBatchDeduction = ((Filter) this.route).getDeduction();

        if (Objects.nonNull(autoBatchDeduction)) {
            MortgageDeduction mortgageDeduction = converter.transToMortgageDeduction(autoBatchDeduction);
            Map<String, List<MortgageDeduction>> split = channelDispatchService.split(mortgageDeduction, null);
            if (split.size() == 1) {
                this.deductionList = split.get(split.keySet().iterator().next());
            } else {
                this.deductionList = Collections.EMPTY_LIST;
            }
        } else {
            this.deductionList = Collections.EMPTY_LIST;
        }


    }
}
