package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 平安商委代扣银行编码
 * @date: Created in 2019/6/27 13:42
 * @Modified By:
 */
public enum PingAnBankCodeEnum {


    /**
     * 平安银行编码枚举
     */
    BANK_CODE_01("0102", "ICBC", "工商银行"),
    BANK_CODE_02("0103", "ABC", "中国农业银行"),
    BANK_CODE_03("0105", "CCB", "中国建设银行"),
    BANK_CODE_04("0104", "BOC", "中国银行"),
    BANK_CODE_06("0309", "CIB", "兴业银行"),
    BANK_CODE_09("0410", "PAB", "平安银行"),
    BANK_CODE_12("0310", "SPDB", "上海浦东发展银行"),
    BANK_CODE_15("0306", "GDB", "广东发展银行");


    /**
     * 银联银行编码
     */
    private String id;
    /**
     * 平安银行编码
     */
    private String code;
    /**
     * 银行名称
     */
    private String bankName;

    PingAnBankCodeEnum(String id, String code, String bankName) {
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
        for (PingAnBankCodeEnum bankCodeEnum : PingAnBankCodeEnum.values()) {
            if (bankName.equals(bankCodeEnum.getBankName())) {
                return bankCodeEnum.getCode();
            }
        }
        return null;
    }

    public static String getIdByCode(String code) {
        for (PingAnBankCodeEnum bankCodeEnum : PingAnBankCodeEnum.values()) {
            if (code.equals(bankCodeEnum.getCode())) {
                return bankCodeEnum.getId();
            }
        }
        return null;
    }


    public static String getCodeById(String id) {
        for (PingAnBankCodeEnum bankCodeEnum : PingAnBankCodeEnum.values()) {
            if (id.equals(bankCodeEnum.getId())) {
                return bankCodeEnum.getCode();
            }
        }
        return null;
    }


    public static String getBankNameById(String id) {
        for (PingAnBankCodeEnum bankCodeEnum : PingAnBankCodeEnum.values()) {
            if (id.equals(bankCodeEnum.getId())) {
                return bankCodeEnum.getBankName();
            }
        }
        return null;
    }

}
