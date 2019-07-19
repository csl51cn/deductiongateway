package org.starlightfinancial.rpc.hessian.entity.yqb.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxResponse;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 平安商委签约查询接口返回信息
 * @date: Created in 2019/6/27 16:50
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx050Response extends AbstractTxResponse {

    private static final long serialVersionUID = 3087617004454413579L;
    /**
     * 	接口版本号，固定为1.0.0
     */
    private String version;
    /**
     * 字符编码,推荐传 UTF-8
     */
    private String charset;
    /**
     * 签名方法，固定为 SHA-256
     */
    private String signMethod;
    /**
     * 签名信息
     */
    private String signature;
    /**
     * 交易类型，本接口为固定值:050
     */
    private String transType;
    /**
     * 是否需要验证短信
     * 只有当应答码为0000时，此字段才有效
     * 1：需要验证短信
     * 0：不需要验证短信，等待异步通知
     */
    private String needSendSMS;
    /**
     * 绑卡ID，当应答码为0156时，此字段一定会出现
     */
    private String bankId;
    /**
     * 商户流水号
     */
    private String merchantSeqNo;
    /**
     * 令牌号，当needSendSMS字段为1，需要验证短信时，此字段一定会出现
     */
    private String traceNo;
    /**
     * 返回码
     */
    private String respCode;
    /**
     * 返回信息
     */
    private String respMsg;

    public Tx050Response() {
    }

    public Tx050Response(String response) throws Exception {
        super(response);
    }


    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        Tx050Response tx050Response = JSONObject.parseObject(responseMessage, Tx050Response.class);
        BeanUtils.copyProperties(tx050Response, this);
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
        return SecurityUtil.encryptWithSHA256(map, PingAnEnvironment.getPlatMerchantKey());
    }

}
