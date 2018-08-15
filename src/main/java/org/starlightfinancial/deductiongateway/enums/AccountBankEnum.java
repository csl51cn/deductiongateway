package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 入账银行
 * @date: Created in 2018/8/9 16:37
 * @Modified By:
 */
public enum AccountBankEnum {

    /**
     * 润通招行0366
     */
    BANK1("招行0366"),
    /**
     * 润通建行3504
     */
    BANK2("建行3504"),
    /**
     * 铠岳建行0334
     */
    BANK3("建行0334"),
    /**
     * 润坤招行0901
     */
    BANK4("招行0901");


    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    AccountBankEnum(String value) {
        this.value = value;
    }
}
