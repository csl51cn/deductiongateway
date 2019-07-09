package org.starlightfinancial.rpc.hessian.entity.yqb.request;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractJsonTxRequest;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @author: Senlin.Deng
 * @Description: 域账户注册请求
 * @date: Created in 2019/7/3 11:59
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TxRegistrationRequest extends AbstractJsonTxRequest {
    private static final long serialVersionUID = 7622426866870830554L;
    /**
     * 发起渠道，平安付分配
     */
    private String channel;
    /**
     * 来源系统名，平安付分配
     */
    private String system;
    /**
     * pc电脑ip，PC不可空
     */
    private String pcIp;
    /**
     * 手机设备号，手机设备不可空
     */
    private String deviceNum;
    /**
     * 手机设备信息，Json格式传递｛”key”:”value”;｝
     */
    private String deviceMessage;
    /**
     * 数据来源，平安付分配
     */
    private String coOperCode;
    /**
     * 对应商户号，平安付分配
     */
    private String merchantNo;
    /**
     * 商户会员编号
     */
    private String partnerId;
    /**
     * 绑定手机号
     */
    private String bindMobile;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 证件类型
     */
    private String identityType;
    /**
     * 证件号码
     */
    private String identityNumber;
    /**
     * 性别，M/F
     */
    private String sex;
    /**
     * 生日，YYYY-MM-DD
     */
    private String birthDate;


    public TxRegistrationRequest() {
        serviceCode = "R0568";
    }

    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    @Override
    public String obtainSendType() {
        return "json";
    }

    /**
     * 处理参数
     *
     * @throws Exception
     */
    @Override
    public void process() throws Exception {
        TreeMap<String, String> map = new TreeMap<>();
        channel = PingAnEnvironment.getRegistrationChannel();
        system = PingAnEnvironment.getRegistrationSystem();
        coOperCode = PingAnEnvironment.getRegistrationCoOperCode();
        map.put("merchantNo", merchantNo);
        map.put("channel", channel);
        map.put("system", system);
        map.put("pcIp", "");
        map.put("deviceNum", "");
        map.put("deviceMessage", "");
        map.put("coOperCode", coOperCode);
        map.put("partnerId", partnerId);
        map.put("bindMobile", bindMobile);
        map.put("realName", realName);
        map.put("identityType", identityType);
        map.put("identityNumber", identityNumber);
        map.put("sex", "");
        map.put("birthDate", "");
        //内容aes加密
        content = SecurityUtil.encryptWithAES(map);
        //请求内容摘要
        token = SecurityUtil.encryptWithSHA1(map);

        HashMap<String, String> hashMap = new HashMap<>(16);
        hashMap.put("content", content);
        hashMap.put("token", token);
        hashMap.put("hashFunc", "SHA-1");
        hashMap.put("serviceCode", serviceCode);
        hashMap.put("systemId", PingAnEnvironment.getRegistrationSystemId());
        requestJsonData = JSONObject.toJSONString(hashMap);
    }
}
