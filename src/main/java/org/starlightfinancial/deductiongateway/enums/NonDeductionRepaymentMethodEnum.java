package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款方式
 * @date: Created in 2018/8/9 16:19
 * @Modified By:
 */
public enum NonDeductionRepaymentMethodEnum {

    /**
     * 银行转账
     */
    BANK_TRANSFER("银行转账"),

    /**
     * 现金
     */
    CASH("现金"),

    /**
     * 通联收银宝
     */
    THIRD_PARTY_PAYMENT("收银宝"),

    /**
     * POS机刷卡
     */
    POS("POS机刷卡");


    private String value;

    NonDeductionRepaymentMethodEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
