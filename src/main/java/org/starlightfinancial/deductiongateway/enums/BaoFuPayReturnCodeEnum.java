package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/25 16:47
 * @Modified By:
 */
public enum BaoFuPayReturnCodeEnum {

    /**
     * 交易已受理
     */
    RETURN_CODE_0000("0000", "交易已受理"),

    /**
     * 商户代付公共参数格式不正确
     */
    RETURN_CODE_0001("0001", "商户代付公共参数格式不正确"),

    /**
     * 商户代付证书无效
     */
    RETURN_CODE_0002("0002", "商户代付证书无效"),

    /**
     * 商户代付报文格式不正确
     */
    RETURN_CODE_0003("0003", "商户代付报文格式不正确"),

    /**
     * 交易请求记录条数超过上限!
     */
    RETURN_CODE_0004("0004", "交易请求记录条数超过上限!"),

    /**
     * 商户代付收款方账号{}被列入黑名单,代付失败
     */
    RETURN_CODE_0205("0205", "商户代付收款方账号{}被列入黑名单");


    private String code;

    private String desc;


    BaoFuPayReturnCodeEnum(String code, String desc) {
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
