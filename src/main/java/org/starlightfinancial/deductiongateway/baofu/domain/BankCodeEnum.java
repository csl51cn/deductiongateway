package org.starlightfinancial.deductiongateway.baofu.domain;

/**
 * Created by sili.chen on 2018/1/3
 */
public enum BankCodeEnum {
    BANK_CODE_01("ICBC", "工商银行"),
    BANK_CODE_02("ABC", "中国农业银行"),
    BANK_CODE_03("CCB", "中国建设银行"),
    BANK_CODE_04("BOC", "中国银行"),
    BANK_CODE_05("BCOM", "中国交通银行"),
    BANK_CODE_06("CIB", "兴业银行"),
    BANK_CODE_07("CITIC", "中信银行"),
    BANK_CODE_08("CEB", "中国光大银行"),
    BANK_CODE_09("PAB", "平安银行"),
    BANK_CODE_10("PSBC", "邮政储蓄银行"),
    BANK_CODE_11("SHB", "上海银行"),
    BANK_CODE_12("SPDB", "上海浦东发展银行"),
    BANK_CODE_13("CMBC", "中国民生银行"),
    BANK_CODE_14("CMB", "招商银行"),
    BANK_CODE_15("GDB", "广东发展银行"),
    BANK_CODE_16("HXB", "华夏银行"),
    BANK_CODE_17("HZB", "杭州银行"),
    BANK_CODE_18("BOB", "北京银行"),
    BANK_CODE_19("NBCB", "宁波银行"),
    BANK_CODE_20("JSB", "江苏银行"),
    BANK_CODE_21("ZSB", "浙商银行");

    private String code;
    private String bankName;

    BankCodeEnum(String code, String bankName) {
        this.code = code;
        this.bankName = bankName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public static String getCodeByBankName(String bankName) {
        for (BankCodeEnum _enum : BankCodeEnum.values()) {
            if (bankName.equals(_enum.getBankName())) {
                return _enum.getCode();
            }
        }
        return null;
    }
}
