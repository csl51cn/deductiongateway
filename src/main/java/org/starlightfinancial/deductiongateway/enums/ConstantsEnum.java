package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: senlin.deng
 * @Description:
 * @date: Created in 2018/5/9 17:06
 * @Modified By:
 */
public enum ConstantsEnum {

    /**
     * 表示肯定,正向意义
     */
    SUCCESS("1", "成功"),

    /**
     * 表示否定,负面意义
     */
    FAIL("0", "失败"),
    /**
     * 请求成功
     */
    REQUEST_SUCCESS("0000", "请求成功"),
    /**
     * 请求失败
     */
    REQUEST_FAIL("0001", "请求失败"),

    /**
     * 无数据返回
     */
    NO_DATA_RESPONSE("2000","无数据返回");


    private String code;
    private String value;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    ConstantsEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
