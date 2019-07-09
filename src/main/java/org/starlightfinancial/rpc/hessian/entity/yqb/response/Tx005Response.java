package org.starlightfinancial.rpc.hessian.entity.yqb.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxResponse;

/**
 * @author: Senlin.Deng
 * @Description: 查询代扣结果返回信息
 * @date: Created in 2019/7/5 13:28
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx005Response extends AbstractTxResponse {

    private static final long serialVersionUID = 16261252471417585L;
    /**
     * 版本号
     */
    private String version;
    /**
     * 枚举类型，目前支持的包括GBK、UTF-8等
     */
    private String charset;
    /**
     * 签名方法
     */
    private String signMethod;
    /**
     * 签名
     */
    private String signature;
    /**
     * 交易类型
     */
    private String transType;
    /**
     * 中间商户号
     */
    private String merchantId;
    /**
     * 平台商户号
     */
    private String platMerchantId;
    /**
     * 商户订单号
     */
    private String mercOrderNo;
    /**
     * 平安付交易号
     */
    private String orderTraceNo;
    /**
     * 原商户订单号 只有退货时才返回此字段
     */
    private String origMercOrderNo;
    /**
     * 原始平安付交易号 只有退货时才返回此字段
     */
    private String origOrderTraceNo;
    /**
     * 原交易类型
     */
    private String origTransType;
    /**
     * 响应码
     */
    private String respCode;
    /**
     * 响应消息
     */
    private String respMsg;
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 错误信息
     */
    private String errMsg;
    /**
     * 响应时间
     */
    private String respTime;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 后台通知URL
     */
    private String backEndUrl;
    /**
     * 前台通知URL
     */
    private String frontEndUrl;
    /**
     * 异常返回URL
     */
    private String cancelUrl;
    /**
     * 交易开始日期时间
     */
    private String orderTime;
    /**
     * Y 重复订单 
     * N 非重复
     */
    private String sameOrderFlag;
    /**
     * 商户自定义交易说明
     */
    private String merchantTransDesc;
    /**
     * 语言
     */
    private String language;
    /**
     * 交易金额 不允许有小数点，如果为CNY，单位为分,取值范围为[1，99999999999999]，如199.88元，则此域为19988
     */
    private String orderAmount;
    /**
     * 交易币种 CNY
     */
    private String orderCurrency;
    /**
     * 分润信息
     */
    private String distributeInfo;
    /**
     * 协议认证号
     */
    private String token;
    /**
     * 用户在商户的会员号
     */
    private String customerMerchantId;
    /**
     * 用户在平安付的会员号
     */
    private String customerPAFId;
    /**
     * 用户姓名
     */
    private String customerName;
    /**
     * 用户证件类型
     */
    private String customerIdType;
    /**
     * 用户证件号码
     */
    private String customerIdNo;
    private String specifiedPayType;
    /**
     * 指定银行编码
     */
    private String specifiedBankNumber;
    /**
     * 交易超时时间
     */
    private String transTimeout;
    /**
     * 防钓鱼时间戳
     */
    private String antiPhishingTimeStamp;
    /**
     * 用户IP
     */
    private String customerIp;
    /**
     * 用户网站refer
     */
    private String customerRefer;
    /**
     * 商户回传参数
     */
    private String mercRetrunPara;
    /**
     * 业务场景
     */
    private String businessScene;
    /**
     * 商户通知保留域
     */
    private String mercNotifyExt;
    /**
     * 商户保留域
     */
    private String merReserved;
    /**
     * 商户保留域2
     */
    private String merReserved2;
    /**
     * 订单支付完成时间
     */
    private String orderCompleteTime;


    public Tx005Response() {
    }

    /**
     * 返回的数据有两种格式,一种是key=value&key=value...,一种是json的格式
     *
     * @param response 返回的数据
     * @throws Exception
     */
    public Tx005Response(String response) throws Exception {
        super(response);
    }

    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        Tx005Response tx005Response = JSONObject.parseObject(responseMessage, Tx005Response.class);
        BeanUtils.copyProperties(tx005Response, this);
    }
}
