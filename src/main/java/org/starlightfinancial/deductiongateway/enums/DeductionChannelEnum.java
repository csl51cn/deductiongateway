package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 代扣渠道
 * @date: Created in 2018/5/29 13:40
 * @Modified By:
 */
public enum DeductionChannelEnum {

    /**
     * 银联
     */
    UNION_PAY("0001", "银联"),
    /**
     * 宝付
     */
    BAO_FU("0002", "宝付");

    private String code;
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

    DeductionChannelEnum(String code, String deductionChannelDesc) {
        this.code = code;
        this.deductionChannelDesc = deductionChannelDesc;
    }

    public static String getCodeByDesc(String certTyeDesc) {
        for (DeductionChannelEnum deductionChannelEnum : DeductionChannelEnum.values()) {
            if (certTyeDesc.equals(deductionChannelEnum.getDeductionChannelDesc())) {
                return deductionChannelEnum.getCode();
            }
        }
        return null;
    }
}
