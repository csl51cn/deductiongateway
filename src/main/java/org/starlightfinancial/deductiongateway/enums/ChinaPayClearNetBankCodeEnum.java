package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 银联银行编码与中金支付银行编码
 * @date: Created in 2019/4/16 15:33
 * @Modified By:
 */
public enum ChinaPayClearNetBankCodeEnum {

    /**
     * 中金银行编码枚举
     */
    BANK_CODE_01("0102", "102", "工商银行"),
    BANK_CODE_02("0103", "103", "中国农业银行"),
    BANK_CODE_03("0105", "105", "中国建设银行"),
    BANK_CODE_04("0104", "104", "中国银行"),
    BANK_CODE_05("0301", "301", "交通银行"),
    BANK_CODE_06("0309", "309", "兴业银行"),
    BANK_CODE_07("0302", "302", "中信银行"),
    BANK_CODE_08("0303", "303", "中国光大银行"),
    BANK_CODE_09("0410", "307", "平安银行"),
    BANK_CODE_10("0100", "100", "邮政储蓄银行"),
    BANK_CODE_11("", "401", "上海银行"),
    BANK_CODE_12("0310", "310", "上海浦东发展银行"),
    BANK_CODE_13("0305", "305", "中国民生银行"),
    BANK_CODE_14("0308", "308", "招商银行"),
    BANK_CODE_15("0306", "306", "广东发展银行"),
    BANK_CODE_16("920", "304", "华夏银行"),
    BANK_CODE_17("910", "441", "重庆银行"),
    BANK_CODE_18("777", "321", "重庆三峡银行"),
    BANK_CODE_19("0910", "1413", "重庆农商行");

    /**
     * 银联银行编码
     */
    private String id;
    /**
     * 中金银行编码
     */
    private String code;
    /**
     * 银行名称
     */
    private String bankName;

    ChinaPayClearNetBankCodeEnum(String id, String code, String bankName) {
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
        for (ChinaPayClearNetBankCodeEnum bankCodeEnum : ChinaPayClearNetBankCodeEnum.values()) {
            if (bankName.equals(bankCodeEnum.getBankName())) {
                return bankCodeEnum.getCode();
            }
        }
        return null;
    }

    public static String getIdByCode(String code) {
        for (ChinaPayClearNetBankCodeEnum bankCodeEnum : ChinaPayClearNetBankCodeEnum.values()) {
            if (code.equals(bankCodeEnum.getCode())) {
                return bankCodeEnum.getId();
            }
        }
        return null;
    }


    public static String getCodeById(String id) {
        for (ChinaPayClearNetBankCodeEnum bankCodeEnum : ChinaPayClearNetBankCodeEnum.values()) {
            if (id.equals(bankCodeEnum.getId())) {
                return bankCodeEnum.getCode();
            }
        }
        return null;
    }


    public static String getBankNameById(String id) {
        for (ChinaPayClearNetBankCodeEnum bankCodeEnum : ChinaPayClearNetBankCodeEnum.values()) {
            if (id.equals(bankCodeEnum.getId())) {
                return bankCodeEnum.getBankName();
            }
        }
        return null;
    }


    public static String getCodeByName(String name) {
        for (ChinaPayClearNetBankCodeEnum bankCodeEnum : ChinaPayClearNetBankCodeEnum.values()) {
            if (name.equals(bankCodeEnum.getBankName())) {
                return bankCodeEnum.getBankName();
            }
        }
        return null;
    }

}
