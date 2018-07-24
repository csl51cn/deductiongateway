package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 还款类别枚举
 * @date: Created in 2018/7/23 17:16
 * @Modified By:
 */
public enum RepaymentTypeEnum {
    /**
     * 本息还款类别
     */
    PRINCIPAL_AND_INTEREST(1212, "本息"),


    /**
     * 调查评估费还款类别
     */
    EVALUATION_FEE(1213, "调查评估费"),

    /**
     * 服务费还款类别
     */
    SERVICE_FEE(1214, "服务费");


    /**
     * 代码
     */
    private int code;
    /**
     * 描述
     */
    private String desc;


    RepaymentTypeEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static Integer getCodeByDesc(String desc) {
        for (RepaymentTypeEnum repaymentTypeEnum : RepaymentTypeEnum.values()) {
            if (desc.equals(repaymentTypeEnum.getDesc())) {
                return repaymentTypeEnum.getCode();
            }
        }
        return null;
    }

}
