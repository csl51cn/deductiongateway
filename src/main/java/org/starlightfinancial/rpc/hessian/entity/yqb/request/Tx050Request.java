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
 * @Description: 平安商委签约请求
 * @date: Created in 2019/6/27 16:03
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx050Request extends AbstractTxRequest {
    private static final long serialVersionUID = -4051392470436026342L;
    /**
     * 接口版本号，固定为1.0.0
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
     * 签名信息，
     */
    private String signature;
    /**
     * 交易类型，本接口固定值:050
     */
    private String transType;
    /**
     * 后台通知URL，接收异步通知地址
     */
    private String backEndUrl;
    /**
     * 银行卡号，3DES加密后的银行卡号
     */
    private String bankEnc;

    /**
     * 银行编码
     */
    private String bankShort;

    /**
     * 用户证件号码
     */
    private String customerIdNo;
    /**
     * 用户证件类型，I --表示居民身份证，目前只支持身份证类型
     */
    private String customerIdType;
    /**
     * 用户姓名
     * 对公账户为公司名
     * 对私为个人姓名
     */
    private String customerName;
    /**
     * 银行预留手机号，定长11
     */
    private String telephone;
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 商户流水号
     */
    private String merchantSeqNo;
    /**
     * 账户类型
     * 0:对私账户
     * 1:对公账户
     */
    private String acctType;
    /**
     * 是否是平安付会员
     * 0：不是平安付会员
     * 1：是平安付会员
     */
    private String pafMember;
    /**
     * 平安付会员号
     * 对公为商户号
     * 对私为会员号
     */
    private String customerPAFId;

    public Tx050Request() {
        version = "1.0.0";
        transType = "050";
        charset = "UTF-8";
        signMethod = "SHA-256";
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
        map.put("backEndUrl", backEndUrl);
        map.put("transType", transType);
        map.put("bankEnc", SecurityUtil.encryptWithDES(bankEnc));
        map.put("bankShort", bankShort);
        map.put("customerIdNo", customerIdNo);
        map.put("customerIdType", customerIdType);
        map.put("customerName", customerName);
        map.put("telephone", telephone);
        map.put("merchantId", merchantId);
        map.put("merchantSeqNo", merchantSeqNo);
        map.put("acctType", acctType);
        map.put("PAFMember", pafMember);
        map.put("customerPAFId", customerPAFId);
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
