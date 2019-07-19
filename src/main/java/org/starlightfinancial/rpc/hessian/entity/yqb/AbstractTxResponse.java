package org.starlightfinancial.rpc.hessian.entity.yqb;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.rpc.hessian.security.yqb.SecurityUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: Senlin.Deng
 * @Description: 抽象的返回信息
 * @date: Created in 2019/6/27 16:41
 * @Modified By:
 */
public abstract class AbstractTxResponse implements Serializable {
    private static final long serialVersionUID = 7493714385078889057L;
    /**
     * json格式的返回消息
     */
    protected String responseMessage;

    /**
     * 返回的code
     */
    protected String code;
    /**
     * 返回的code的说明信息
     */
    protected String message;

    public AbstractTxResponse() {
    }

    /**
     * 返回的数据有两种格式,一种是key=value&key=value...,一种是json的格式
     *
     * @param response 返回的数据
     * @throws Exception
     */
    public AbstractTxResponse(String response) throws Exception {
        //验签,将返回的信息并转换为json
        if (response.startsWith("{")) {
            //json格式的返回
            handleWithJsonFormat(response);
        } else {
            //key-value格式的返回
            handleWithKeyValueFormat(response);
        }
        process();
    }

    /**
     * 处理key-value形式的返回
     *
     * @param response 返回的数据
     */
    private void handleWithKeyValueFormat(String response) throws Exception {
        TreeMap<String, String> resultMap = Stream.of(response.split("&")).map(keyValue -> keyValue.split("=")).collect(Collectors.toMap(item -> item[0], item -> item.length > 1 ? item[1] : "", (u, v) -> {
            throw new IllegalStateException(String.format("Duplicate key %s", u));
        }, TreeMap::new));
        String responseCode = resultMap.get("respCode");
        if ("0000".equals(responseCode)) {
            String responseSignature = resultMap.get("signature");
            //重新对数据签名
            String signature = verySign((TreeMap) resultMap.clone());
            //判断签名是否相等
            if (StringUtils.equals(responseSignature, signature)) {
                responseMessage = JSONObject.toJSONString(resultMap);
                code = resultMap.get("respCode");
                message = resultMap.get("respMsg");
            } else {
                throw new Exception("验证签名失败。");
            }
        } else {
            responseMessage = JSONObject.toJSONString(resultMap);
            code = resultMap.get("respCode");
            message = resultMap.get("respMsg");
        }
    }

    /**
     * 处理json形式的返回
     *
     * @param response 返回的数据
     * @return
     */
    private void handleWithJsonFormat(String response) throws Exception {
        JSONObject jsonObject = JSONObject.parseObject(response);
        String content = jsonObject.getString("content");
        String contentDecrypted = SecurityUtil.decryptASE(content);
        jsonObject.putAll(JSONObject.parseObject(contentDecrypted));
        responseMessage = jsonObject.toJSONString();
        code = jsonObject.getString("respCode");
        message = jsonObject.getString("memo");
    }


    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    protected abstract void process() throws Exception;

    /**
     * 验签
     *
     * @param map
     * @return
     * @throws Exception
     */
    protected abstract String verySign(Map<String, String> map) throws Exception;


    public String getResponseMessage() {
        return responseMessage;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }
}
