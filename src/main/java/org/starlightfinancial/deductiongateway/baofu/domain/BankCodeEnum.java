package org.starlightfinancial.deductiongateway.baofu.domain;

/**
 * Created by sili.chen on 2018/1/3
 */
public enum BankCodeEnum {
    BANK_CODE_01("0102", "ICBC", "工商银行"),
    BANK_CODE_02("0103", "ABC", "中国农业银行"),
    BANK_CODE_03("0105", "CCB", "中国建设银行"),
    BANK_CODE_04("0104", "BOC", "中国银行"),
    BANK_CODE_05("", "BCOM", "中国交通银行"),
    BANK_CODE_06("0309", "CIB", "兴业银行"),
    BANK_CODE_07("0302", "CITIC", "中信银行"),
    BANK_CODE_08("0303", "CEB", "中国光大银行"),
    BANK_CODE_09("0410", "PAB", "平安银行"),
    BANK_CODE_10("0100", "PSBC", "邮政储蓄银行"),
    BANK_CODE_11("", "SHB", "上海银行"),
    BANK_CODE_12("0310", "SPDB", "上海浦东发展银行"),
    BANK_CODE_13("0305", "CMBC", "中国民生银行"),
    BANK_CODE_14("0308", "CMB", "招商银行"),
    BANK_CODE_15("0306", "GDB", "广东发展银行"),
    BANK_CODE_16("920", "HXB", "华夏银行"),
    BANK_CODE_17("", "HZB", "杭州银行"),
    BANK_CODE_18("", "BOB", "北京银行"),
    BANK_CODE_19("", "NBCB", "宁波银行"),
    BANK_CODE_20("", "JSB", "江苏银行"),
    BANK_CODE_21("", "ZSB", "浙商银行");

    private String id;
    private String code;
    private String bankName;

    BankCodeEnum(String id, String code, String bankName) {
        this.id = id;
        this.code = code;
        this.bankName = bankName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static String getIdByCode(String code) {
        for (BankCodeEnum _enum : BankCodeEnum.values()) {
            if (code.equals(_enum.getCode())) {
                return _enum.getId();
            }
        }
        return null;
    }
}
