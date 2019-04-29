package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.service.AssemblerFactory;
import org.starlightfinancial.deductiongateway.service.Delivery;
import org.starlightfinancial.deductiongateway.service.Handler;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.math.BigDecimal;
import java.util.Collections;
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
    AssemblerFactory assemblerFactory;

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
            Assembler assembler = assemblerFactory.getAssembleImpl("auto");
            splitter.setRoute(metadataValidator);
            filter.setRoute(splitter);
            assembler.setRoute(filter);
            delivery.setRoute(assembler);
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
        splitter.setAutoBatchDeduction(autoBatchDeduction);
        //如果是建行,并且扣款金额>10万,剔除掉不进行自动代扣
        boolean exclude = StringUtils.equals(BankCodeEnum.BANK_CODE_03.getBankName(), autoBatchDeduction.getBankName())
                && autoBatchDeduction.getBxAmount().add(autoBatchDeduction.getFwfAmount()).compareTo(BigDecimal.valueOf(100000)) > 0;
        if (exclude) {
            return Collections.EMPTY_LIST;
        }
        return this.handleRequest();
    }
}
