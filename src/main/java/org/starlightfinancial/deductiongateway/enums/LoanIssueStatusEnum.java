package org.starlightfinancial.deductiongateway.enums;

import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 转账状态枚举, 后期有其他渠道继续添加其他的code
 * @date: Created in 2018/9/25 16:12
 * @Modified By:
 */
public enum LoanIssueStatusEnum {


    /**
     * 转账处理中
     */
    STATUS0("0", "0", "转账处理中"),

    /**
     * 转账成功
     */
    STATUS1("1", "1", "转账成功"),

    /**
     * 转账失败
     */
    STATUS2("2", "-1", "转账失败"),


    /**
     * 退款
     */
    STATUS3("3", "2", "转账退款"),

    /**
     * 部分成功
     */
    STATUS4("4", "", "转账部分成功");


    private String code;
    private String baoFuCode;
    private String desc;

    LoanIssueStatusEnum(String code, String baoFuCode, String desc) {
        this.code = code;
        this.baoFuCode = baoFuCode;
        this.desc = desc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBaoFuCode() {
        return baoFuCode;
    }

    public void setBaoFuCode(String baoFuCode) {
        this.baoFuCode = baoFuCode;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public static String getCodeByBaoFuCode(String baoFuCode) {
        for (LoanIssueStatusEnum loanIssueStatusEnum : LoanIssueStatusEnum.values()) {
            if (baoFuCode.equals(loanIssueStatusEnum.getBaoFuCode())) {
                return loanIssueStatusEnum.getCode();
            }
        }
        return null;
    }

    public static String getDescByCode(String code) {
        if (Objects.isNull(code)){
            return null;
        }
        for (LoanIssueStatusEnum loanIssueStatusEnum : LoanIssueStatusEnum.values()) {
            if (code.equals(loanIssueStatusEnum.getCode())) {
                return loanIssueStatusEnum.getDesc();
            }
        }
        return null;
    }


}
