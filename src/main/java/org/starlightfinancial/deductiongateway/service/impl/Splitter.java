package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.domain.local.LimitManagerRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Decorator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/8/23
 */
@Component
public class Splitter extends Decorator {

    private AutoBatchDeduction autoBatchDeduction;

    private List<AutoBatchDeduction> deductionList = new ArrayList<>();

    @Autowired
    private LimitManagerRepository limitManagerRepository;

    @Value("${batch.route.use}")
    private String router;

    public void splitter() {
        deductionList.clear();
        // 宝付不分账
        if (router.equals("BAOFU")) {
            deductionList.add(autoBatchDeduction);
            return;
        }
        // 银联分账
        LimitManager limitManager = limitManagerRepository.findByBankName(autoBatchDeduction.getBankName());
        BigDecimal singleLimit = limitManager.getSingleLimit();
        BigDecimal bxAmount = autoBatchDeduction.getBxAmount();
        BigDecimal fwfAmount = autoBatchDeduction.getFwfAmount();

        if (bxAmount.add(fwfAmount).doubleValue() <= singleLimit.doubleValue() || singleLimit.doubleValue() == -1.0) {
            deductionList.add(autoBatchDeduction);
        } else {
            deductionList = recurLimit(singleLimit);
        }
    }

    private List recurLimit(BigDecimal singleLimit) {
        List result = new ArrayList();
        BigDecimal totalAmount = autoBatchDeduction.getBxAmount().add(autoBatchDeduction.getFwfAmount());
        BigDecimal drawDownAmount = totalAmount;
        BigDecimal drawDownBxAmount = autoBatchDeduction.getBxAmount();
        BigDecimal drawDownFwfAmount = autoBatchDeduction.getFwfAmount();

        while (drawDownAmount.doubleValue() > 0) {
            BigDecimal bxAmount;
            if (drawDownAmount.doubleValue() > singleLimit.doubleValue()) {
                bxAmount = singleLimit.subtract(drawDownFwfAmount);
                if (bxAmount.doubleValue() >= singleLimit.doubleValue()) {
                    bxAmount = singleLimit;
                }
            } else {
                bxAmount = drawDownAmount;
            }
            AutoBatchDeduction newObj = new AutoBatchDeduction();
            BeanUtils.copyProperties(autoBatchDeduction, newObj);
            newObj.setBxAmount(bxAmount);
            newObj.setFwfAmount(drawDownFwfAmount);
            drawDownAmount = drawDownAmount.subtract(bxAmount).subtract(drawDownFwfAmount);
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
            drawDownFwfAmount = drawDownFwfAmount.subtract(drawDownFwfAmount);

            result.add(newObj);
        }

        return result;
    }

    @Override
    public void doRoute() throws Exception {
        super.doRoute();
        splitter();
    }

    public List getDeductionList() {
        return deductionList;
    }

    public void setAutoBatchDeduction(AutoBatchDeduction autoBatchDeduction) {
        this.autoBatchDeduction = autoBatchDeduction;
    }
}
