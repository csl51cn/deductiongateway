package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.DefaultChannel;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.service.DefaultChannelService;
import org.starlightfinancial.deductiongateway.utility.BeanConverter;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by sili.chen on 2017/8/23
 */
@Component
public class Splitter extends Decorator {

    @Autowired
    private ChannelDispatchService channelDispatchService;
    @Autowired
    private BeanConverter converter;

    @Autowired
    private DefaultChannelService defaultChannelService;
    private List<MortgageDeduction> deductionList;

    public List<MortgageDeduction> getDeductionList() {
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
            boolean matchLimit = total.compareTo(BigDecimal.valueOf(50000)) >= 0 && total.compareTo(BigDecimal.valueOf(100000)) <= 0;
            this.deductionList = new ArrayList<>();
            DefaultChannel defaultChannel = defaultChannelService.getByBankCode(mortgageDeduction.getParam1());
            //如果一条记录中同时有本息和服务费,分离本息和服务费
            List<MortgageDeduction> mortgageDeductions = splitPrincipalInterestAndServiceFee(mortgageDeduction);
            String channelString;
            if (Objects.nonNull(defaultChannel) && StringUtils.isNotBlank(DeductionChannelEnum.getOrderDescByCode(defaultChannel.getDefaultChannel()))) {
                //如果默认渠道记录不为空,并且设置的默认渠道是有效的,直接使用
                channelString = defaultChannel.getDefaultChannel();
            } else if (isICBC && isHoliday & matchLimit) {
                //在节假日中,工商银行扣款金额在[5万,10万]范围,使用中金代扣
                channelString = DeductionChannelEnum.CHINA_PAY_CLEAR_NET_DEDUCTION.getCode();
            } else {
                //没有指定渠道
                channelString = null;
            }

            for (MortgageDeduction deduction : mortgageDeductions) {
                //根据限额拆分记录
                Map<String, List<MortgageDeduction>> stringListMap = channelDispatchService.split(deduction, channelString);
                stringListMap.forEach((channel, list) -> deductionList.addAll(list));
            }
        } else {
            this.deductionList = Collections.EMPTY_LIST;
        }
    }

    /**
     * 分割本息和服务费.如果一条代扣记录有本息和服务费,需要将它分成两条,一条只有本息,一条只有服务费.
     *
     * @return 返回list
     */
    private List<MortgageDeduction> splitPrincipalInterestAndServiceFee(MortgageDeduction mortgageDeduction) {
        //同时有本息和服务费,需要拆分
        boolean needSplit = mortgageDeduction.getSplitData1().compareTo(BigDecimal.ZERO) > 0 && mortgageDeduction.getSplitData2().compareTo(BigDecimal.ZERO) > 0;
        ArrayList<MortgageDeduction> mortgageDeductions = new ArrayList<MortgageDeduction>();
        if (needSplit) {
            MortgageDeduction newDeduction = mortgageDeduction.cloneSelf();
            //设置原来代扣记录的服务费为0
            mortgageDeduction.setSplitData2(BigDecimal.ZERO);
            //设置复制的代扣记录的本息为0
            newDeduction.setSplitData1(BigDecimal.ZERO);
            mortgageDeductions.add(mortgageDeduction);
            mortgageDeductions.add(newDeduction);
        } else {
            mortgageDeductions.add(mortgageDeduction);
        }
        return mortgageDeductions;
    }


}
