package org.starlightfinancial.deductiongateway.domain.local;

/**
 * 银联失败交易返回状态码
 * Created by sili.chen on 2017/8/17
 */
public enum ErrorCodeEnum {
    CODE_000001("1001", "成功", "0000"),
    CODE_000002("2006", "系统处理失败", "3359"),
    CODE_000003("20ST", "已撤销", "20ST"),
    CODE_000004("2030", "报文内容检查错或者处理错", "2030"),
    CODE_000005("20PZ", "无法查询到该交易，可以重发", "3034"),
    CODE_000006("2000", "系统正在对数据处理", "2000"),
    CODE_000007("2012", "等待商户审核", "2012"),
    CODE_000008("20QB", "商户审核不通过", "20QB"),
    CODE_000009("2013", "等待CP受理", "2013"),
    CODE_000010("20QB", "CP不通过受理", "20QB"),
    CODE_000011("2009", "提交银行处理", "2009"),
    CODE_000012("2001", "查开户方原因", "3201"),
    CODE_000013("2014", "无效卡号", "2061"),
    CODE_000014("2051", "余额不足", "2064"),
    CODE_000015("2061", "累计交易金额超限", "3013"),
    CODE_000016("2041", "已挂失折", "2041"),
    CODE_000017("20P9", "账户已冻结", "20P9"),
    CODE_000018("20QS", "系统忙，请稍后再提交", "20QS"),
    CODE_000019("20E2", "数字签名或证书错", "20E2"),
    CODE_000020("2031", "无路由或路由参数有误", "2031"),
    CODE_000021("2022", "交易失败", "2022"),
    CODE_000022("2094", "重复业务", "2094"),
    CODE_000023("2003", "无效商户", "2003"),
    CODE_000024("20PU", "订单号错误", "20PU"),
    CODE_000025("2030", "格式错误", "2030"),
    CODE_000026("2009", "提交成功，等待银行处理", "2009"),
    CODE_000027("20F3", "卡号与证件号码不符", "3302"),
    CODE_000028("20PD", "账户未加办代收付标志", "20PD"),
    CODE_000029("20PS", "户名不符", "20PS"),
    CODE_000030("20Q3", "日期错误", "20Q3"),
    CODE_000031("20PZ", "原交易信息不存在", "20PZ"),
    CODE_000032("20T4", "未签约账户", "20T4"),
    CODE_000033("20FF", "非白名单卡号", "20FF"),
    CODE_000034("20TY", "ip不通过", "20TY"),
    CODE_000035("0001", "查询失败", "0001"),
    CODE_000036("2013", "币种错误", "2013"),
    CODE_000037("20EC", "商户状态不合法", "20EC"),
    CODE_000038("20R1", "退款处理中", "20R1"),
    CODE_000039("1003", "退款成功", "1003"),
    CODE_000040("9999", "系统处理失败", "9999"),
    CODE_000041("2062", "该储种不能办理代收付业务", "3305"),
    CODE_000042("0003", "消费交易失败", "1111"),
    CODE_000043("2075", "密码输错次数超限，请联系发卡行", "3389"),
    CODE_000044("9999", "系统处理失败", "3251"),
    CODE_000045("2036","受限制的卡","3233");


    private String code;
    private String value;
    private String newCode;

    private ErrorCodeEnum(String code, String value, String newCode) {
        this.code = code;
        this.value = value;
        this.newCode = newCode;
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

    public String getNewCode() {
        return newCode;
    }

    public void setNewCode(String newCode) {
        this.newCode = newCode;
    }

    public static String getValueByCode(String code) {
        for (ErrorCodeEnum _enum : ErrorCodeEnum.values()) {
            if (code.equals(_enum.getCode())) {
                return _enum.getValue();
            }
        }

        return null;
    }

    public static String getCodeByNewCode(String newCode) {
        for (ErrorCodeEnum _enum : ErrorCodeEnum.values()) {
            if (newCode.equals(_enum.getNewCode())) {
                return _enum.getCode();
            }
        }

        return null;
    }

    public static String getValueByNewCode(String newCode) {
        for (ErrorCodeEnum _enum : ErrorCodeEnum.values()) {
            if (newCode.equals(_enum.getNewCode())) {
                return _enum.getValue();
            }
        }

        return null;
    }
}
