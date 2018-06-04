package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 代扣渠道
 * @date: Created in 2018/5/29 13:40
 * @Modified By:
 */
public enum DeductionChannelEnum {

    /**
     * 银联快捷支付
     */
    CHINA_PAY_QUICK_PAY("0001", "银联", "银联快捷支付"),
    /**
     * 宝付协议支付
     */
    BAO_FU_AGREEMENT_PAY("0002", "宝付", "宝付协议支付"),

    /**
     * 银联白名单代扣
     */
    CHINA_PAY_CLASSIC_DEDUCTION("0003", "银联", "银联旧代扣"),

    /**
     * 银联新无卡代扣
     */
    CHINA_PAY_NEW_DEDUCTION("0004", "银联", "银联新无卡代扣"),

    /**
     * 宝付代扣
     */
    BAO_FU_CLASSIC_DEDUCTION("0005", "宝付", "宝付旧代扣"),;


    /**
     * 代扣渠道代码
     */
    private String code;
    /**
     * 订单描述
     */
    private String orderDesc;
    /**
     * 代扣渠道描述
     */
    private String deductionChannelDesc;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDeductionChannelDesc() {
        return deductionChannelDesc;
    }


    public void setDeductionChannelDesc(String deductionChannelDesc) {
        this.deductionChannelDesc = deductionChannelDesc;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    DeductionChannelEnum(String code, String orderDesc, String deductionChannelDesc) {
        this.code = code;
        this.orderDesc = orderDesc;
        this.deductionChannelDesc = deductionChannelDesc;
    }

    public static String getCodeByDesc(String desc) {
        for (DeductionChannelEnum deductionChannelEnum : DeductionChannelEnum.values()) {
            if (desc.equals(deductionChannelEnum.getDeductionChannelDesc())) {
                return deductionChannelEnum.getCode();
            }
        }
        return null;
    }
}
