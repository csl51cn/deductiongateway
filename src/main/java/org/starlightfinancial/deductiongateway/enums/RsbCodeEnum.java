package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: senlin.deng
 * @Description: 润通服务总线返回的状态码
 * @date: Created in 2018/5/9 16:50
 * @Modified By:
 */
public enum RsbCodeEnum {

    ERROR_CODE_01("000000", "请求成功"),
    ERROR_CODE_02("300000", "无数据"),
    ERROR_CODE_03("300001", "请求失败"),
    ERROR_CODE_04("300002", ""),
    ERROR_CODE_05("300003", ""),
    ERROR_CODE_06("300004", "访问频率过快"),
    ERROR_CODE_07("300005", "无权限访问此api"),
    ERROR_CODE_08("300006", ""),
    ERROR_CODE_09("300007", ""),
    ERROR_CODE_10("300008", "缺少必要参数"),
    ERROR_CODE_11("300009", ""),
    ERROR_CODE_12("300010", "URL不存在"),
    ERROR_CODE_13("300011", "缺少resp_code参数"),
    ERROR_CODE_14("300012", "缺少dgtl_envlp参数"),
    ERROR_CODE_15("300013", "json字符串解析失败"),
    ERROR_CODE_16("300014", "Base64编码异常"),
    ERROR_CODE_17("300015", "代付交易宝付明文返回结果");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    RsbCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
