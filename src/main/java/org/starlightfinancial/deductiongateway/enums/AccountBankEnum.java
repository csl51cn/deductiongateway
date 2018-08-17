package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 入账银行对应业务系统收款银行开户行
 * @date: Created in 2018/8/9 16:37
 * @Modified By:
 */
public enum AccountBankEnum {

    /**
     * 润通入账银行:招行0366
     */
    RUN_TONG_CMBC_0356("2094", "招行0366"),
    /**
     * 润通入账银行:建行3504
     */
    RUN_TONG_CCB_3504("2095", "建行3504"),
    /**
     * 铠岳入账银行:建行0334
     */
    KAI_YUE_CCB_0334("2096", "建行0334"),
    /**
     * 润坤入账银行:招行0901
     */
    RUN_KUN_CMBC_0901("2097", "招行0901"),
    /**
     * 润坤入账银行:招行0702
     */
    RUN_KUN_CMBC_0702("2098", "招行0702");


    private String code;
    private String value;

    AccountBankEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static String getCodeByValue(String value) {
        if (value != null) {
            for (AccountBankEnum accountBankEnum : AccountBankEnum.values()) {
                if (value.equals(accountBankEnum.getValue())) {
                    return accountBankEnum.getCode();
                }
            }
        }
        return null;
    }


}
