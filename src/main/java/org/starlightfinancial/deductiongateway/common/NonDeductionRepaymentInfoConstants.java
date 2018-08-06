package org.starlightfinancial.deductiongateway.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息相关参数
 * @date: Created in 2018/8/1 15:58
 * @Modified By:
 */
public class NonDeductionRepaymentInfoConstants {

    /**
     * 入账公司
     */
    public static final List<String> CHARGE_COMPANY = new ArrayList<String>(3) {
        {
            add("润通");
            add("铠岳");
            add("润坤");
        }
    };

    /**
     * 还款方式
     */
    public static final List<String> REPAYMENT_METHOD = new ArrayList<String>(4) {
        {
            add("银行转账");
            add("现金");
            add("收银宝");
            add("POS机刷卡");
        }
    };

    /**
     * 还款方式
     */
    public static final List<String> REPAYMENT_TYPE = new ArrayList<String>(4) {
        {
            add("本息");
            add("服务费");
            add("调查评估费");
            add("其它");
        }
    };

    /**
     * 还款方式
     */
    public static final List<String> BANK = new ArrayList<String>(4) {
        {
            add("招行0366");
            add("建行3504");
            add("建行0334");
            add("招行0901");
        }
    };


}
