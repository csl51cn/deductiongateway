package org.starlightfinancial.rpc.hessian.entity.yqb.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxRequest;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * @author: Senlin.Deng
 * @Description: 代扣请求参数
 * @date: Created in 2019/7/4 16:41
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Tx001Request extends AbstractTxRequest {

    private static final long serialVersionUID = 3475090401071230974L;

    /**
     * 接口版本号，固定为2.0.0
     */
    private String version;
    /**
     * 字符编码	示例：UTF-8
     */
    private String charset;
    /**
     * 签名方法，固定为 SHA-256	本属性为：SHA-256
     */
    private String signMethod;

    /**
     * 签名信息
     */
    private String signature;
    /**
     * 交易类型	本属性为：001
     */
    private String transType;
    /**
     * 交易代码	本属性为：0107
     */
    private String transCode;
    /**
     * 业务类型，用于识别具体业务类型	本属性为：000001
     */
    private String bizType;
    /**
     * 商户号（平安付分配）
     */
    private String merchantId;
    /**
     * 商户号（平安付分配）	可空
     */
    private String platMerchantId;
    /**
     * 后台通知URL
     */
    private String backEndUrl;
    /**
     * 前台通知URL	可空
     */
    private String frontEndUrl;
    /**
     * 交易开始日期时间 yyyyMMddHHmmss
     */
    private String orderTime;
    /**
     * Y 重复订单
     * N 非重复
     * 默认为 N
     */
    private String sameOrderFlag;
    /**
     * 商户订单号
     */
    private String mercOrderNo;
    /**
     * 商户自定义交易说明
     */
    private String merchantTransDesc;
    /**
     * 语言 如果不传，默认为中文
     */
    private String language;
    /**
     * 交易金额，不允许有小数点，如果为CNY，单位为分,取值范围为[1，99999999999999]，如199.88元，则此域为19988
     */
    private String orderAmount;
    /**
     * 交易币种，如不传，则默认CNY
     */
    private String orderCurrency;
    /**
     * 用户平安付帐户号
     * 用户在平安付的帐户号，如果有的话，建议商户提交
     */
    private String customerPAFId;
    /**
     * 交易超时时间，未付款交易的超时时间，一旦超时，该笔交易就会自动被关闭， 
     * m-分钟，h-小时，d-天
     * 该参数数值不接受小数点，如2.5h，可转换为150m
     */
    private String transTimeout;
    /**
     * 商户回传参数 可空
     */
    private String mercRetrunPara;
    /**
     * 业务场景	本属性为：0001
     */
    private String businessScene;
    /**
     * 商户保留域	可空
     */
    private String merReserved;
    /**
     * 商户保留域2	可空
     */
    private String merReserved2;
    /**
     * 分账串  [{"merchantId":"900000112606","merchantType":"B","splitOrderNo":"sub1_153098721487613625254315846","splitAmt":"80000","commAmt":"0"},
     * {"merchantId":"1000010003702181","merchantType":"C","splitOrderNo":"sub2_1998842716571336551213","splitAmt":"20000","commAmt":"0"}]
     * splitOrderNo:分账流水号
     * merchantId :分账商户号
     * merchantType:商户类型(B:商户，C:个人) 
     * commAmt:分佣金额(单位：分)
     * splitAmt: 分账金额(单位：分)
     * 注：分账金额包含分佣金额
     * 分账金额相加等于订单金额
     */
    private String combinePayInfo;
    /**
     * 风控信息
     */
    private String riskInfo;
    /**
     * 是否实名认证	是：Y 否：N
     */
    private String isCertified;
    /**
     * 客户姓名
     * isCertified=Y时必传
     */
    private String customerName;
    /**
     * 证件类型	示例：I （身份证）
     * isCertified=Y时必传
     */
    private String customerIdType;
    /**
     * 证件号码
     * isCertified=Y时必传
     */
    private String customerIdNo;
    /**
     * 指定支付方式	示例：WITHCONTRACTNO^80000^FP900000113194000000017021259694 
     * 指定支付方式^金额^协议号或者绑卡ID
     * WITHBANKID：通过绑卡ID方式扣款
     * WITHCONTRACTNO：通过协议号方式进行扣款
     */
    private String specifiedPayType;

    public Tx001Request() {
        version = "2.0.0";
        charset = "UTF-8";
        signMethod = "SHA-256";
        transType = "001";
        transCode = "0107";
        bizType = "000001";
        orderCurrency = "CNY";
        businessScene = "0001";

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
        map.put("transCode", transCode);
        map.put("bizType", bizType);
        map.put("merchantId", merchantId);
        map.put("platMerchantId", platMerchantId);
        map.put("backEndUrl", backEndUrl);
        map.put("frontEndUrl", frontEndUrl);
        map.put("orderTime", orderTime);
        map.put("sameOrderFlag", sameOrderFlag);
        map.put("mercOrderNo", mercOrderNo);
        map.put("merchantTransDesc", merchantTransDesc);
        map.put("language", language);
        map.put("orderAmount", orderAmount);
        map.put("orderCurrency", orderCurrency);
        map.put("customerPAFId", customerPAFId);
        map.put("transTimeout", transTimeout);
        map.put("mercRetrunPara", mercRetrunPara);
        map.put("businessScene", businessScene);
        map.put("merReserved", merReserved);
        map.put("merReserved2", merReserved2);
        map.put("combinePayInfo", combinePayInfo);
        map.put("riskInfo", riskInfo);
        map.put("isCertified", isCertified);
        map.put("customerName", customerName);
        map.put("customerIdType", customerIdType);
        map.put("customerIdNo", customerIdNo);
        map.put("specifiedPayType", specifiedPayType);
        signature = SecurityUtil.encryptWithSHA256(map, PingAnEnvironment.getMerchantKey());
        map.put("signMethod", signMethod);
        map.put("signature", signature);
        map.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v)) {
                requestMessage.add(new BasicNameValuePair(k, v));
            }
        });


    }
}
