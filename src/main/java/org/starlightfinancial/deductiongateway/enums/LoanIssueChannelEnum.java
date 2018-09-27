package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放渠道
 * @date: Created in 2018/9/18 16:52
 * @Modified By:
 */
public enum LoanIssueChannelEnum {
    /**
     * 宝付渠道
     */
    BAO_FU("1001","宝付");




    private String code;
    private String desc;

    LoanIssueChannelEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
