package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放银行code与服务商的银行名对应关系枚举
 * @date: Created in 2018/9/25 10:26
 * @Modified By:
 */
public enum LoanIssueBankEnum {
    /**
     * 工商银行
     */
    BANK518(518, "工商银行"),

    /**
     * 农业银行
     */
    BANK519(519, "农业银行"),

    /**
     * 招商银行
     */
    BANK520(520, "招商银行"),

    /**
     * 中信银行
     */
    BANK521(521, "中信银行"),

    /**
     * 光大银行
     */
    BANK522(522, "光大银行"),

    /**
     * 广发银行
     */
    BANK523(523, "广发银行"),


    /**
     * 民生银行
     */
    BANK524(524, "民生银行"),

    /**
     * 兴业银行
     */
    BANK525(525, "兴业银行"),


    /**
     * 邮政储蓄银行
     */
    BANK526(526, "邮政储蓄银行"),

    /**
     * 中国银行
     */
    BANK527(527, "中国银行"),

    /**
     * 建设银行
     */
    BANK1043(1043, "建设银行"),

    /**
     * 浦发银行
     */
    BANK1048(1048, "浦发银行"),

    /**
     * 交通银行
     */
    BANK1269(1269, "交通银行"),

    /**
     * 重庆农村商业银行
     */
    BANK1272(1272, "重庆农村商业银行"),

    /**
     * 重庆农村商业银行
     */
    BANK1273(1273, "重庆三峡银行"),

    /**
     * 平安银行
     */
    BANK1274(1274, "平安银行"),

    /**
     * 华夏银行
     */
    BANK2086(2086, "华夏银行"),

    /**
     * 江苏银行
     */
    BANK2110(2110, "江苏银行"),

    /**
     * 重庆银行
     */
    BANK2274(2274, "重庆银行"),
    ;


    private Integer code;
    private String baoFuBank;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getBaoFuBank() {
        return baoFuBank;
    }

    public void setBaoFuBank(String baoFuBank) {
        this.baoFuBank = baoFuBank;
    }

    LoanIssueBankEnum(Integer code, String baoFuBank) {
        this.code = code;
        this.baoFuBank = baoFuBank;
    }

    public static String getBankNameByCode(Integer code) {
        for (LoanIssueBankEnum loanIssueBankEnum : LoanIssueBankEnum.values()) {
            if (code.equals(loanIssueBankEnum.getCode())) {
                return loanIssueBankEnum.getBaoFuBank();
            }
        }
        return null;
    }
}
