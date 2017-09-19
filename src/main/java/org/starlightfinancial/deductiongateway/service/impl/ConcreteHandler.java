package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.local.ErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.service.AssemblerFactory;
import org.starlightfinancial.deductiongateway.service.Handler;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by sili.chen on 2017/8/23
 */
public class ConcreteHandler extends Handler implements ItemProcessor {

    @Autowired
    Splitter splitter;

    @Autowired
    AssemblerFactory assemblerFactory;

    @Autowired
    MetadataValidator metadataValidator;

    @Autowired
    HttpClientUtil httpClientUtil;

    private AutoBatchDeduction autoBatchDeduction;

    @Override
    public List<MortgageDeduction> handleRequest() {
        List<MortgageDeduction> handleResult = new ArrayList();
        try {
            Assembler assembler = assemblerFactory.getAssembleImpl("auto");
            splitter.setRoute(metadataValidator);
            assembler.setRoute(splitter);
            assembler.doRoute();

            List<GoPayBean> result = assembler.getResult();
            for (GoPayBean goPayBean : result) {
                Map map = httpClientUtil.send(goPayBean.aggregationToList());
//                Map map = new HashMap();
                String payStat = (String) map.get("PayStat");
                MortgageDeduction mortgageDeduction = goPayBean.transToMortgageDeduction();
                mortgageDeduction.setResult(payStat);
                if (StringUtils.equals(Constant.SUCCESS, payStat)) {
                    mortgageDeduction.setIssuccess("1");
                } else {
                    payStat = "9999";
                    mortgageDeduction.setIssuccess("0");
                }
                mortgageDeduction.setErrorResult(ErrorCodeEnum.getValueByCode(payStat));
                handleResult.add(mortgageDeduction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return handleResult;
    }

    @Override
    public Object process(Object o) throws Exception {
        this.autoBatchDeduction = (AutoBatchDeduction) o;
        splitter.setAutoBatchDeduction(autoBatchDeduction);
        metadataValidator.setAutoBatchDeduction(autoBatchDeduction);
        return this.handleRequest();
    }
}
