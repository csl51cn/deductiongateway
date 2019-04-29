package org.starlightfinancial.deductiongateway.service.impl;

import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: SiliChen
 * @Description:
 * @Date: Created in 9:43 2019/4/29
 * @Modified By:
 */
public class TestSplite {

    public static void main(String[] args) {
        cal(new BigDecimal(5000));
    }

    public static void cal(BigDecimal singleLimit) {
        List result = new ArrayList();
        BigDecimal drawDownBxAmount = new BigDecimal(3601);
        BigDecimal drawDownFwfAmount = new BigDecimal(1400);
        drawDownBxAmount = dealFwf(drawDownBxAmount, drawDownFwfAmount, singleLimit, result);
        System.out.println(drawDownBxAmount.doubleValue());
        dealBx(drawDownBxAmount, singleLimit, result);
    }

    private static void dealBx(BigDecimal drawDownBxAmount, BigDecimal singleLimit, List result) {
        BigDecimal bxAmount;
        BigDecimal fwfAmount = new BigDecimal(0);
        while (drawDownBxAmount.doubleValue() > 0) {
            if (drawDownBxAmount.doubleValue() >= singleLimit.doubleValue()) {
                bxAmount = singleLimit;
            } else {
                bxAmount = drawDownBxAmount;
            }
            AutoBatchDeduction newObj = new AutoBatchDeduction();
            newObj.setBxAmount(bxAmount);
            newObj.setFwfAmount(fwfAmount);
            result.add(newObj);
            System.out.println(newObj.getBxAmount().doubleValue() + " @ " + newObj.getFwfAmount().doubleValue());
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
        }
    }

    private static BigDecimal dealFwf(BigDecimal drawDownBxAmount, BigDecimal drawDownFwfAmount, BigDecimal singleLimit, List list) {
        BigDecimal bxAmount;
        BigDecimal fwfAmount;
        while (drawDownFwfAmount.doubleValue() > 0) {
            if (drawDownFwfAmount.doubleValue() >= singleLimit.doubleValue()) {
                bxAmount = new BigDecimal(0);
                fwfAmount = singleLimit;
            } else {
                if (drawDownBxAmount.doubleValue() >= singleLimit.subtract(drawDownFwfAmount).doubleValue()) {
                    bxAmount = singleLimit.subtract(drawDownFwfAmount);
                } else {
                    bxAmount = drawDownBxAmount;
                }
                fwfAmount = drawDownFwfAmount;
            }
            AutoBatchDeduction newObj = new AutoBatchDeduction();
            newObj.setBxAmount(bxAmount);
            newObj.setFwfAmount(fwfAmount);
            list.add(newObj);
            System.out.println(newObj.getBxAmount().doubleValue() + " @ " + newObj.getFwfAmount().doubleValue());
            drawDownBxAmount = drawDownBxAmount.subtract(bxAmount);
            drawDownFwfAmount = drawDownFwfAmount.subtract(fwfAmount);
        }
        return drawDownBxAmount;
    }
}
