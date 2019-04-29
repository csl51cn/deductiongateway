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

        if (bxAmount.add(fwfAmount).doubleValue() <= singleLimit.doubleValue() || singleLimit.doubleValue() == -1.0 || singleLimit.doubleValue() == 0) {
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
        drawDownBxAmount = dealFwf(drawDownBxAmount, drawDownFwfAmount, singleLimit, result);
        dealBx(drawDownBxAmount, singleLimit, result);
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

    /**
     * @param drawDownBxAmount  1
     * @param drawDownFwfAmount 2
     * @param singleLimit       3
     * @param list              4
     * @return java.math.BigDecimal
     * @Author sili.chen
     * @Description 处理服务费
     * @Date 14:48 2019/4/29
     **/
    private BigDecimal dealFwf(BigDecimal drawDownBxAmount, BigDecimal drawDownFwfAmount, BigDecimal singleLimit, List list) {
        BigDecimal bxAmount;
        BigDecimal fwfAmount;
        while (drawDownFwfAmount.doubleValue() > 0) {
            if (drawDownFwfAmount.doubleValue() >= singleLimit.doubleValue()) {
                bxAmount = new BigDecimal(0);
                fwfAmount = singleLimit;
            } else {
                bxAmount = singleLimit.subtract(drawDownFwfAmount);
                fwfAmount = drawDownFwfAmount;
            }
            AutoBatchDeduction newObj = new AutoBatchDeduction();
            BeanUtils.copyProperties(autoBatchDeduction, newObj);
            newObj.setBxAmount(bxAmount);
            newObj.setFwfAmount(fwfAmount);
            list.add(newObj);
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
            drawDownFwfAmount = drawDownFwfAmount.subtract(fwfAmount);
        }
        return drawDownBxAmount;
    }

    /**
     * @param drawDownBxAmount 1
     * @param singleLimit      2
     * @param result           3
     * @return void
     * @Author sili.chen
     * @Description 处理本息
     * @Date 14:48 2019/4/29
     **/
    private void dealBx(BigDecimal drawDownBxAmount, BigDecimal singleLimit, List result) {
        BigDecimal bxAmount;
        BigDecimal fwfAmount = new BigDecimal(0);
        while (drawDownBxAmount.doubleValue() > 0) {
            if (drawDownBxAmount.doubleValue() >= singleLimit.doubleValue()) {
                bxAmount = singleLimit;
            } else {
                bxAmount = drawDownBxAmount;
            }
            AutoBatchDeduction newObj = new AutoBatchDeduction();
            BeanUtils.copyProperties(autoBatchDeduction, newObj);
            newObj.setBxAmount(bxAmount);
            newObj.setFwfAmount(fwfAmount);
            result.add(newObj);
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
        }
    }
}
