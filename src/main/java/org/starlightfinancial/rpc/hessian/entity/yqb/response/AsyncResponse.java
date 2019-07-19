package org.starlightfinancial.rpc.hessian.entity.yqb.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxResponse;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 异步返回信息
 * @date: Created in 2019/7/8 13:57
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AsyncResponse extends AbstractTxResponse {


    private static final long serialVersionUID = 1389551517690522724L;
    /**
     * 版本号
     */
    private String version;
    /**
     * 字符编码
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
     * 通知ID
     */
    private String notifyId;
    /**
     * 通知类型
     */
    private String notifyType;
    /**
     * 通知时间，格式：yyyyMMddHHmmss
     */
    private String notifyTime;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户订单/流水号
     */
    private String mercOrderNo;
    /**
     * 平安付交易号，该域是IAPS系统给予每笔交易的唯一标识
     */
    private String orderTraceNo;
    /**
     * 订单创建时间，格式：yyyyMMddHHmmss
     */
    private String orderCreateTime;
    /**
     * 订单支付完成时间，格式：yyyyMMddHHmmss
     */
    private String orderPayTime;
    /**
     * 交易关闭时间，格式：yyyyMMddHHmmss
     */
    private String orderCloseTime;
    /**
     * 交易开始时间，格式：yyyyMMddHHmmss
     */
    private String orderTime;
    /**
     * 平台商户号
     */
    private String platMerchantId;
    /**
     * 交易金额，不允许有小数点，如果为CNY，单位为分,取值范围为[1，99999999999999]
     */
    private String orderAmount;
    /**
     * 交易币种，如不传，则默认CN
     */
    private String orderCurrency;
    /**
     * 商户回传参数，如果用户请求时传递了该参数，则返回给商户时会回传该参数
     */
    private String mercRetrunPara;
    /**
     * 支付失败返回码
     */
    private String errCode;
    /**
     * 支付失败时返回
     */
    private String errMsg;
    /**
     * 商户通知保留域
     */
    private String mercNotifyExt;
    /**
     * 保留域，后续扩展使用
     */
    private String retReserved;


    public AsyncResponse() {
    }

    /**
     * 返回的数据有两种格式,一种是key=value&key=value...,一种是json的格式
     *
     * @param response 返回的数据
     * @throws Exception
     */
    public AsyncResponse(String response) throws Exception {
        super(response);
    }

    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        AsyncResponse asyncResponse = JSONObject.parseObject(responseMessage, AsyncResponse.class);
        BeanUtils.copyProperties(asyncResponse, this);
    }

    /**
     * 验签
     *
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    protected String verySign(Map<String, String> map) throws Exception {
        String merchantId = map.get("merchantId");
        if (StringUtils.equals(merchantId, PingAnEnvironment.getMerchantId())) {
            return SecurityUtil.encryptWithSHA256(map, PingAnEnvironment.getMerchantKey());
        } else if (StringUtils.equals(merchantId, PingAnEnvironment.getPlatMerchantId())) {
            return SecurityUtil.encryptWithSHA256(map, PingAnEnvironment.getMerchantKey());
        }
        return null;
    }
}
