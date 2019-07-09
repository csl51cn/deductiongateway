package org.starlightfinancial.rpc.hessian.entity.yqb.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxResponse;

/**
 * @author: Senlin.Deng
 * @Description: 代扣返回信息
 * @date: Created in 2019/7/4 16:53
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx001Response extends AbstractTxResponse {

    private static final long serialVersionUID = 2280290923924287667L;
    /**
     * 接口版本号，固定为2.0.0
     */
    private String version;
    /**
     * 签名方法，固定为 SHA-256
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
     * 交易代码
     */
    private String transCode;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 交易开始日期时间
     * yyyyMMddHHmmss
     */
    private String orderTime;
    /**
     * 交易号
     */
    private String transId;
    /**
     * 订单支付完成时间
     * yyyyMMddHHmmss
     */
    private String orderCompleteTime;
    /**
     * 返回码
     */
    private String respCode;
    /**
     * 返回码解释信息
     */
    private String respMsg;
    /**
     * 商户订单号
     */
    private String mercOrderNo;
    /**
     * 平安付交易号
     * 给予每笔交易的唯一标识
     */
    private String orderTraceNo;
    /**
     * 平安付分账响应流水号
     */
    private String combinePayRes;
    /**
     * 商户回传参数
     */
    private String mercRetrunPara;
    /**
     * 商户保留域
     */
    private String merReserved;
    /**
     * 商户保留域2
     */
    private String merReserved2;


    public Tx001Response() {
    }

    /**
     * 返回的数据有两种格式,一种是key=value&key=value...,一种是json的格式
     *
     * @param response 返回的数据
     * @throws Exception
     */
    public Tx001Response(String response) throws Exception {
        super(response);
    }

    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        Tx001Response tx001Response = JSONObject.parseObject(responseMessage, Tx001Response.class);
        BeanUtils.copyProperties(tx001Response, this);
    }
}
