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
 * @Description: 平安商委签约短信验证返回信息
 * @date: Created in 2019/6/27 17:15
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx047Response extends AbstractTxResponse {

    private static final long serialVersionUID = -8986978177845548799L;
    /**
     * 接口版本号，固定为1.0.0
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
     * 签名信息
     */
    private String signature;
    /**
     * 交易类型
     */
    private String transType;
    /**
     * 绑卡Id	当商业委托接口中PAFMembe为1时，一定会返回
     */
    private String bankId;
    /**
     * 协议号定长，当商业委托接口中PAFMembe为0时，一定会返回
     */
    private String token;
    /**
     * 商户流水号
     */
    private String merchantSeqNo;
    /**
     * 平安付会员号
     */
    private String customerPAFId;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 返回码
     */
    private String respCode;
    /**
     * 返回信息
     */
    private String respMsg;

    public Tx047Response() {
    }

    public Tx047Response(String response) throws Exception {
        super(response);
    }

    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        Tx047Response tx047Response = JSONObject.parseObject(responseMessage, Tx047Response.class);
        BeanUtils.copyProperties(tx047Response, this);
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
