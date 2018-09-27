package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 入账公司
 * @date: Created in 2018/8/9 16:17
 * @Modified By:
 */
public enum ChargeCompanyEnum {
    /**
     * 润通
     */
    RUN_TONG("润通"),

    /**
     * 铠岳
     */
    KAI_YUE("铠岳"),

    /**
     * 润坤
     */
    RUN_KUN("润坤"),

    /**
     * 第三方咨询公司
     */
    THIRD_PARTY_CONSULT("第三方咨询公司");

    private String value;

    ChargeCompanyEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
