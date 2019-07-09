package org.starlightfinancial.rpc.hessian.entity.yqb.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxRequest;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author: Senlin.Deng
 * @Description: 交易查询请求参数
 * @date: Created in 2019/7/5 10:57
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx005Request extends AbstractTxRequest {


    private static final long serialVersionUID = 4572389354982161975L;
    /**
     * 版本号，固定2.0.1
     */
    private String version;

    /**
     * 枚举类型，目前支持的包括GBK、UTF-8等
     */
    private String charset;
    /**
     * 签名方法，枚举类型，在交易应答中该域内容应与交易请求一致。 目前支持的签名算法包括SHA-256
     */
    private String signMethod;
    /**
     * 签名
     */
    private String signature;
    /**
     * 交易类型 本接口使用005
     */
    private String transType;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 平台商户号
     */
    private String platMerchantId;
    /**
     * 商户订单号 mercOrderNo和orderTraceNo两个字段必须出现一个，示例：201903180001
     */
    private String mercOrderNo;
    /**
     * 平安付交易号 互联网收单系统每笔交易的惟一标识
     */
    private String orderTraceNo;

    public Tx005Request() {
        version = "2.0.1";
        charset = "UTF-8";
        signMethod = "SHA-256";
        transType = "005";
    }

    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    @Override
    public String obtainSendType() {
        return "form";
    }

    /**
     * 处理参数
     *
     * @throws Exception
     */
    @Override
    public void process() throws Exception {
        requestMessage = new ArrayList<>();
        TreeMap<String, String> map = new TreeMap<>();
        map.put("version", version);
        map.put("charset", charset);
        map.put("transType", transType);
        map.put("merchantId", merchantId);
        map.put("platMerchantId", platMerchantId);
        map.put("mercOrderNo", mercOrderNo);
        map.put("orderTraceNo", orderTraceNo);
        signature = SecurityUtil.encryptWithSHA256(map);
        map.put("signature", signature);
        map.put("signMethod", signMethod);
        map.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v)) {
                requestMessage.add(new BasicNameValuePair(k, v));
            }
        });
    }
}
