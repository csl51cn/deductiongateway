package org.starlightfinancial.rpc.hessian.security.yqb;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * @Author: SiliChen
 * @Description:
 * @Date: Created in 9:20 2019/6/27
 * @Modified By:
 */
public class SecurityUtil {


    /**
     * 对字符串加密,加密算法使用MD5,SHA-1,SHA-256,默认使用SHA-1
     *
     * @param strSrc  要加密的字符串
     * @param encName 加密类型
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encryptPwd(String strSrc, String encName,
                                    String charset) throws UnsupportedEncodingException {
        if (strSrc == null) {
            return null;
        }
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes(charset);
        try {
            if (encName == null || "".equals(encName)) {
                encName = "SHA-1";
            }
            md = MessageDigest.getInstance(encName);
            md.update(bt);
            // to HexString
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    public static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp = null;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString();
    }


    /**
     * 加密方法:使用SHA-256
     *
     * @param map 加密方法:使用SHA-256
     * @param merchantKey 商户秘钥
     * @return
     * @throws Exception
     */
    public static String encryptWithSHA256(Map<String, String> map,String merchantKey) throws Exception {
        StringBuilder stringBuilder = new StringBuilder();
        String charset = map.get("charset");
        String signMethod = map.remove("signMethod");
        map.remove("signature");
        map.remove("transId");
        map.forEach((k, v) -> {
            if (StringUtils.isNotBlank(v)) {
                stringBuilder.append("&").append(k).append("=").append(v);
            }
        });
        stringBuilder.append(merchantKey);
        //去除掉第一个"&"
        String data = stringBuilder.toString().substring(1);
        signMethod = StringUtils.isBlank(signMethod) ? "SHA-256" : signMethod;
        charset = StringUtils.isBlank(charset) ? "UTF-8" : charset;
        return encryptPwd(data, signMethod, charset);
    }

    /**
     * 加密方法:使用SHA-1
     *
     * @param map 待加密参数
     */
    public static String encryptWithSHA1(Map<String, String> map) throws Exception {
        return encryptPwd(JSONObject.toJSONString(map) + PingAnEnvironment.getMerchantSHA1Key(), "SHA-1", "UTF-8");
    }

    /**
     * 加密方法:使用AES
     *
     * @param map 待加密参数
     */
    public static String encryptWithAES(Map<String, String> map) throws Exception {
        return AESEncoder.encrypt4Aes(JSONObject.toJSONString(map));
    }

    /**
     * 加密方法:使用3DES加密
     * @param merchantKey 商户秘钥
     * @param data
     * @return
     */
    public static String encryptWithDES(String data,String merchantKey) throws Exception {
        return DESEncrypt.encrypt(data,merchantKey);
    }

    /**
     * 解密ASE
     *
     * @param data
     * @return
     */
    public static String decryptASE(String data) {
        return AESDecoder.decrypt4Aes2Str(data);
    }
}
