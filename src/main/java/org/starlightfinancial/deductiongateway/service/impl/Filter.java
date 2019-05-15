package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.baofu.domain.BankCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Decorator;
import org.starlightfinancial.deductiongateway.utility.Constant;

import java.math.BigDecimal;

/**
 * Created by sili.chen on 2017/11/29
 */
@Component
public class Filter extends Decorator {



    @Autowired
    AccountManagerRepository accountManagerRepository;

    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        filter();
    }


    private void filter() {
        //如果是建行,并且扣款金额>10万,剔除掉不进行自动代扣
        boolean exclude = StringUtils.equals(BankCodeEnum.BANK_CODE_03.getBankName(), autoBatchDeduction.getBankName())
                && autoBatchDeduction.getBxAmount().add(autoBatchDeduction.getFwfAmount()).compareTo(BigDecimal.valueOf(100000)) > 0;
        if (exclude) {
            log.info("自动代扣剔除建行超过10万记录,合同编号:{},客户名称:{}",autoBatchDeduction.getContractNo(),autoBatchDeduction.getCustomerName());
            autoBatchDeduction = null;
            return;
        }
        //如果没有开启自动代扣剔除掉
        AccountManager accountManager = accountManagerRepository.findByAccountAndSortAndContractNo(this.autoBatchDeduction.getAccout(),
                1, this.autoBatchDeduction.getContractNo());
        if (null != accountManager && Constant.ENABLED_FALSE.equals(accountManager.getIsEnabled())) {
            log.info("剔除未开启自动代扣记录,合同编号:{},客户名称:{}",autoBatchDeduction.getContractNo(),autoBatchDeduction.getCustomerName());
            autoBatchDeduction = null;
        }
    }
    private AutoBatchDeduction autoBatchDeduction;
    public void setAutoBatchDeduction(AutoBatchDeduction autoBatchDeduction) {
        this.autoBatchDeduction = autoBatchDeduction;
    }

    private static final Logger log = LoggerFactory.getLogger(Filter.class);

    public AutoBatchDeduction getDeduction() {
        return this.autoBatchDeduction;
    }
}
