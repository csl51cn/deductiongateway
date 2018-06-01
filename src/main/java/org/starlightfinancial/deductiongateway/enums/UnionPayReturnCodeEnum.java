package org.starlightfinancial.deductiongateway.enums;

/**
 * @author: Senlin.Deng
 * @Description: 银联响应码
 * @date: Created in 2018/5/16 15:12
 * @Modified By:
 */
public enum UnionPayReturnCodeEnum {


    UNIONPAY_CODE_001("0000", "成功"),

    UNIONPAY_CODE_002("0001", "初始状态"),

    UNIONPAY_CODE_003("0003", "消费交易失败"),

    UNIONPAY_CODE_004("0006", "签约失败"),

    UNIONPAY_CODE_005("0007", "重复签约"),

    UNIONPAY_CODE_006("0009", "退款交易失败"),

    UNIONPAY_CODE_007("0012", "交易撤销成功"),

    UNIONPAY_CODE_008("0014", "数据接收成功"),

    UNIONPAY_CODE_009("0024", "退款撤销成功"),

    UNIONPAY_CODE_010("0025", "重复交易"),

    UNIONPAY_CODE_011("0026", "预授权完成处理成功"),

    UNIONPAY_CODE_012("0029", "预授权撤销成功"),

    UNIONPAY_CODE_013("0031", "退款撤销审核不通过"),

    UNIONPAY_CODE_014("0037", "预授权完成撤销成功"),

    UNIONPAY_CODE_015("1002", "商户审核不通过"),

    UNIONPAY_CODE_016("1003", "商户已审核"),

    UNIONPAY_CODE_017("1005", "交易撤销中"),

    UNIONPAY_CODE_018("1007", "退款撤销中"),

    UNIONPAY_CODE_019("1008", "预授权完成处理中"),

    UNIONPAY_CODE_020("1009", "预授权撤销中"),

    UNIONPAY_CODE_021("1010", "重汇已申请"),

    UNIONPAY_CODE_022("1011", "重汇审核通过"),

    UNIONPAY_CODE_023("1012", "重汇审核不通过"),

    UNIONPAY_CODE_024("1013", "退款成功(重汇)"),

    UNIONPAY_CODE_025("1014", "预授权完成撤销中"),

    UNIONPAY_CODE_026("1015", "交易发送成功"),

    UNIONPAY_CODE_027("1016", "交易发送失败"),

    UNIONPAY_CODE_028("1017", "初始发送状态"),

    UNIONPAY_CODE_029("1018", "生成支付账单号成功"),

    UNIONPAY_CODE_030("1019", "经办成功"),

    UNIONPAY_CODE_031("1021", "单边账已退款"),

    UNIONPAY_CODE_032("1022", "商户已申请"),

    UNIONPAY_CODE_033("1026", "退款调账已申请"),

    UNIONPAY_CODE_034("1027", "退款调账成功"),

    UNIONPAY_CODE_035("1028", "重汇文件已下载"),

    UNIONPAY_CODE_036("1029", "重汇确认失败"),

    UNIONPAY_CODE_037("1030", "财务已审核"),

    UNIONPAY_CODE_038("1049", "退款预终止"),

    UNIONPAY_CODE_039("1050", "退款已终止"),

    UNIONPAY_CODE_040("1099", "风险交易"),

    UNIONPAY_CODE_041("2001", "报文解析失败"),

    UNIONPAY_CODE_042("2002", "无效的令牌"),

    UNIONPAY_CODE_043("2003", "卡已过期"),

    UNIONPAY_CODE_044("2004", "请求频繁"),

    UNIONPAY_CODE_045("2006", "交易超时"),

    UNIONPAY_CODE_046("2007", "获取动态验证码失败"),

    UNIONPAY_CODE_047("2015", "IP地址非法"),

    UNIONPAY_CODE_048("2016", "非法服务请求"),

    UNIONPAY_CODE_049("2017", "平台校验失败"),

    UNIONPAY_CODE_050("2018", "无效证书"),

    UNIONPAY_CODE_051("2027", "商户支付机构信息表无记录"),

    UNIONPAY_CODE_052("2028", "查询系统配置表无记录"),

    UNIONPAY_CODE_053("2031", "非法商户"),

    UNIONPAY_CODE_054("2032", "CVN2失效"),

    UNIONPAY_CODE_055("2034", "重复退款"),

    UNIONPAY_CODE_056("2036", "交易报文信息不一致"),

    UNIONPAY_CODE_057("2040", "订单数据已同步,请至新菜单做退款"),

    UNIONPAY_CODE_058("2042", "格式校验失败"),

    UNIONPAY_CODE_059("2043", "系统异常,请查询后处理"),

    UNIONPAY_CODE_060("2044", "基本格式校验失败-字段非空未填"),

    UNIONPAY_CODE_061("2045", "基本格式校验失败-字段类型错误"),

    UNIONPAY_CODE_062("2046", "基本格式校验失败-字段长度错误"),

    UNIONPAY_CODE_063("2049", "风控受限"),

    UNIONPAY_CODE_064("2061", "银行卡非法"),

    UNIONPAY_CODE_065("2064", "资金不足"),

    UNIONPAY_CODE_066("2066", "手机号已失效"),

    UNIONPAY_CODE_067("2067", "手机号格式错误"),

    UNIONPAY_CODE_068("2071", "无效短信码"),

    UNIONPAY_CODE_069("2402", "原交易判定失败"),

    UNIONPAY_CODE_070("3003", "验签失败"),

    UNIONPAY_CODE_071("3004", "防钓鱼校验失败"),

    UNIONPAY_CODE_072("3007", "订单有效期校验失败"),

    UNIONPAY_CODE_073("3010", "商户未开通此交易类型"),

    UNIONPAY_CODE_074("3011", "单笔交易超限"),

    UNIONPAY_CODE_075("3012", "累计交易笔数超限"),

    UNIONPAY_CODE_076("3013", "累计交易金额超限"),

    UNIONPAY_CODE_077("3014", "单笔交易超限-商户"),

    UNIONPAY_CODE_078("3015", "累计交易笔数超限-商户"),

    UNIONPAY_CODE_079("3016", "累计交易金额超限-商户"),

    UNIONPAY_CODE_080("3019", "分账方和订单商户非分账关系"),

    UNIONPAY_CODE_081("3020", "分账金额与订单金额不符"),

    UNIONPAY_CODE_082("3022", "该笔订单已经支付成功请查实"),

    UNIONPAY_CODE_083("3034", "未找到原始交易"),

    UNIONPAY_CODE_084("3201", "查发卡方"),

    UNIONPAY_CODE_085("3229", "不正确的PIN"),

    UNIONPAY_CODE_086("3223", "受限制的卡"),

    UNIONPAY_CODE_087("3239", "允许的输入PIN次数超限"),

    UNIONPAY_CODE_088("3249", "交换中心转发了原交易请求，但未收到发卡方应答"),

    UNIONPAY_CODE_089("3251", "受理方状态非法"),

    UNIONPAY_CODE_090("3264", "已发送银行"),

    UNIONPAY_CODE_091("3270", "不支持此类卡交易"),

    UNIONPAY_CODE_092("3285", "预授权号不匹配"),

    UNIONPAY_CODE_093("3289", "预授权完成金额不匹配"),

    UNIONPAY_CODE_094("3290", "预授权取消、完成交易不得超过原交易30天以上"),

    UNIONPAY_CODE_095("3293", "交易结果未知"),

    UNIONPAY_CODE_096("3295", "累计退货金额大于原交易金额"),

    UNIONPAY_CODE_097("3302", "卡号与证件号码不符"),

    UNIONPAY_CODE_098("3308", "该储种不能办理代收付业务"),

    UNIONPAY_CODE_099("3309", "帐户已销户"),

    UNIONPAY_CODE_100("3310", "账户已冻结"),

    UNIONPAY_CODE_101("3325", "密码错误"),

    UNIONPAY_CODE_102("3326", "户名不符"),

    UNIONPAY_CODE_103("3333", "原交易信息记录不存在"),

    UNIONPAY_CODE_104("3346", "银行分户存款余额不足"),

    UNIONPAY_CODE_105("3359", "交易提交银行错误请与ChinaPay系统管理员联系"),

    UNIONPAY_CODE_106("3389", "密码输错次数超限，请联系发卡行"),

    UNIONPAY_CODE_107("3397", "未开通银联无卡支付业务"),

    UNIONPAY_CODE_108("3400", "路由失败"),

    UNIONPAY_CODE_109("3401", "地区信息错误"),

    UNIONPAY_CODE_110("3417", "签名错误"),

    UNIONPAY_CODE_111("3422", "撤销交易的日期不是当天的日期"),

    UNIONPAY_CODE_112("3427", "交易金额超出待支付金额"),

    UNIONPAY_CODE_113("3432", "处理失败"),

    UNIONPAY_CODE_114("3441", "无流水"),

    UNIONPAY_CODE_115("3444", "商户日期与系统日期相差超过一天，拒绝交易"),

    UNIONPAY_CODE_116("3445", "交易币种非法"),

    UNIONPAY_CODE_117("3446", "原交易已做过退款，撤销拒绝"),

    UNIONPAY_CODE_118("3448", "授权码校验不一致"),

    UNIONPAY_CODE_119("3450", "退款交易状态非失败，退款撤销拒绝"),

    UNIONPAY_CODE_120("3452", "商户未配置所属收单机构"),

    UNIONPAY_CODE_121("3455", "商户未关联分账商户"),

    UNIONPAY_CODE_122("3456", "该商户不是分账商户"),

    UNIONPAY_CODE_123("3458", "商户费用分账数据未配置"),

    UNIONPAY_CODE_124("3459", "分账数据错误"),

    UNIONPAY_CODE_125("3460", "分账订单已分账"),

    UNIONPAY_CODE_126("3461", "非延时分账交易"),

    UNIONPAY_CODE_127("3462", "非分账交易"),

    UNIONPAY_CODE_128("3463", "批量-明细信息不匹配"),

    UNIONPAY_CODE_129("4364", "支付机构号不支持前台交易"),

    UNIONPAY_CODE_130("3468", "商户未关联在该接入机构下"),

    UNIONPAY_CODE_131("3470", "未查询到签约信息或签约信息已变更"),

    UNIONPAY_CODE_132("3473", "渠道无流水");

    private String code;

    private String value;

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    UnionPayReturnCodeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValueByCode(String code) {
        for (UnionPayReturnCodeEnum unionPayReturnCodeEnum : UnionPayReturnCodeEnum.values()) {
            if (code.equals(unionPayReturnCodeEnum.getCode())) {
                return unionPayReturnCodeEnum.getValue();
            }
        }
        return null;
    }
}
