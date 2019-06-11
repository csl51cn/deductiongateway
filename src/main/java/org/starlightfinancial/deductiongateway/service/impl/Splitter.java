package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.time.LocalDate;
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
            //如果在节假日中,工商银行扣款金额在[5万,10万]范围,使用中金代扣
            boolean isICBC = StringUtils.equals(mortgageDeduction.getParam1(), "0102");
            boolean isHoliday = Utility.isHoliday(LocalDate.now());
            BigDecimal total = mortgageDeduction.getSplitData1().add(mortgageDeduction.getSplitData2());
            boolean matchLimit = total.compareTo(BigDecimal.valueOf(50000)) > 0 && total.compareTo(BigDecimal.valueOf(100000)) < 0;
            Map<String, List<MortgageDeduction>> split;
            if (isICBC && isHoliday & matchLimit) {
                split = channelDispatchService.split(mortgageDeduction, DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getCode());
            } else {
                split = channelDispatchService.split(mortgageDeduction, null);
            }
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
