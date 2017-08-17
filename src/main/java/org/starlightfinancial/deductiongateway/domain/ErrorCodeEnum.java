package org.starlightfinancial.deductiongateway.domain;

/**
 * 银联失败交易返回状态码
 * Created by sili.chen on 2017/8/17
 */
public enum ErrorCodeEnum {
    CODE_000001("1001", "处理完成或接受成功"),
    CODE_000002("2006", "系统处理失败"),
    CODE_000003("20ST", "已撤销"),
    CODE_000004("2030", "报文内容检查错或者处理错"),
    CODE_000005("20PZ", "无法查询到该交易，可以重发"),
    CODE_000006("2000", "系统正在对数据处理"),
    CODE_000007("2012", "等待商户审核"),
    CODE_000008("20QB", "商户审核不通过"),
    CODE_000009("2013", "等待CP受理"),
    CODE_000010("20QB", "CP不通过受理"),
    CODE_000011("2009", "提交银行处理"),
    CODE_000012("2001", "查开户方原因"),
    CODE_000013("2014", "无效卡号"),
    CODE_000014("2051", "余额不足"),
    CODE_000015("2061", "超出提款限额"),
    CODE_000016("2041", "已挂失折"),
    CODE_000017("20P9", "账户已冻结"),
    CODE_000018("20QS", "系统忙，请稍后再提交"),
    CODE_000019("20E2", "数字签名或证书错"),
    CODE_000020("2031", "无路由或路由参数有误"),
    CODE_000021("2022", "交易失败"),
    CODE_000022("2094", "重复业务"),
    CODE_000023("2003", "无效商户"),
    CODE_000024("20PU", "订单号错误"),
    CODE_000025("2030", "格式错误"),
    CODE_000026("2009", "提交成功，等待银行处理"),
    CODE_000027("20F3", "累计退货金额大于原交易金额"),
    CODE_000028("20PD", "账户未加办代收付标志"),
    CODE_000029("20PS", "户名不符"),
    CODE_000030("20Q3", "日期错误"),
    CODE_000031("20PZ", "原交易信息不存在"),
    CODE_000032("20T4", "未签约账户"),
    CODE_000033("20FF", "非白名单卡号"),
    CODE_000034("20TY", "ip不通过"),
    CODE_000035("0001", "查询失败"),
    CODE_000036("2013", "币种错误"),
    CODE_000037("20EC", "商户状态不合法"),
    CODE_000038("20R1", "退款处理中"),
    CODE_000039("1003", "退款成功"),
    CODE_000040("9999", "系统处理失败");

    private String code;
    private String value;

    private ErrorCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

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

    public static String getValueByCode(String code) {
        for (ErrorCodeEnum _enum : ErrorCodeEnum.values()) {
            if (code.equals(_enum.getCode())) {
                return _enum.getValue();
            }
        }

        return null;
    }
}
