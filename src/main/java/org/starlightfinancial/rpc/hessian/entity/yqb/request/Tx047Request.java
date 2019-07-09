package org.starlightfinancial.rpc.hessian.entity.yqb.request;

import com.alibaba.fastjson.JSONObject;
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
 * @Description: 平安商委签约短信验证请求参数
 * @date: Created in 2019/6/27 16:57
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx047Request extends AbstractTxRequest {

    private static final long serialVersionUID = -6708763447442578821L;
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
     * 签名信息
     */
    private String signature;
    /**
     * 交易类型，固定值:047
     */
    private String transType;
    /**
     * 银行预留手机，定长
     */
    private String telephone;
    /**
     * 加密的银行卡号
     */
    private String bankEnc;
    /**
     * 用户姓名
     */
    private String customerName;
    /**
     * 用户证件号码
     */
    private String customerIdNo;
    /**
     * 卡类型	贷记卡-C、借记卡-D，备注（准贷记卡视为贷记卡）
     */
    private String cardType;
    /**
     * 信用卡CVN	如果卡类型是贷记卡则可能需要提供CVN，有效年月，注:CVN及有效期非必填,根据接口返回的返回码，确认支付要素再次调用接口.需要进行3DES加密
     */
    private String bankCvn;
    /**
     * 有效年
     */
    private String expiredYear;
    /**
     * 有效月
     */
    private String expiredMonth;
    /**
     * 商户流水号
     */
    private String merchantSeqNo;
    /**
     * 商户号（平安付分配）
     */
    private String merchantId;
    /**
     * 商户保留域:此字段格式为json ，json字段说明：
     * customerPAFId:平安付会员号，当商业委托接口PAFMember字段为1时为必传；
     * traceNo：
     * 令牌号，商业委托接口返回，必传；
     * verifyCode：短信验证码,必传
     * 示例：{“customerPAFId”:””,
     * "traceNo":"28311_13210859925",
     * "verifyCode":"123456"};
     */
    private String merReserved;

    /**
     * 令牌号，商业委托接口返回，必传
     */
    private String traceNo;
    /**
     * 短信验证码,必传
     */
    private String verifyCode;
    /**
     * 平安付会员号,当商业委托接口PAFMember字段为1时为必传；
     */
    private String customerPAFId;


    public Tx047Request() {
        version = "1.0.0";
        transType = "047";
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
        map.put("transType", transType);
        map.put("telephone", telephone);
        map.put("bankEnc", SecurityUtil.encryptWithDES(bankEnc));
        map.put("customerIdNo", customerIdNo);
        map.put("customerName", customerName);
        map.put("cardType", cardType);
        map.put("bankCvn", bankCvn);
        map.put("expiredYear", expiredYear);
        map.put("expiredMonth", expiredMonth);
        map.put("merchantId", merchantId);
        map.put("merchantSeqNo", merchantSeqNo);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("customerPAFId", customerPAFId);
        jsonObject.put("traceNo", traceNo);
        jsonObject.put("verifyCode", verifyCode);
        merReserved = jsonObject.toJSONString();
        map.put("merReserved", merReserved);
        signature = SecurityUtil.encryptWithSHA256(map);
        map.put("signMethod", signMethod);
        map.put("signature", signature);
        map.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v)) {
                requestMessage.add(new BasicNameValuePair(k, v));
            }
        });

    }

}
