package org.starlightfinancial.rpc.hessian.security.yqb;

import org.starlightfinancial.rpc.hessian.config.PingAnEnvironment;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @Author: SiliChen
 * @Description:报文解密
 * @Date: Created in 10:38 2019/6/27
 * @Modified By:
 */
public class AESDecoder {

    /**
     * @param s 1
     * @return byte[]
     * @Author sili.chen
     * @Description 将 BASE64 编码的字符串 s 进行解码
     * @Date 10:39 2019/6/27
     **/
    public static byte[] base64decode(String s) {
        if (s == null) return null;
        sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(s);
            return b;
        } catch (Exception e) {
            return null;
        }
    }

    public static byte[] getKeyByStr(String str) {
        byte[] bRet = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            Integer itg = new Integer(16 * getChrInt(str.charAt(2 * i)) + getChrInt(str.charAt(2 * i + 1)));
            bRet[i] = itg.byteValue();
        }
        return bRet;
    }

    public static int getChrInt(char chr) {
        int iRet = 0;
        if (chr == "0".charAt(0)) iRet = 0;
        if (chr == "1".charAt(0)) iRet = 1;
        if (chr == "2".charAt(0)) iRet = 2;
        if (chr == "3".charAt(0)) iRet = 3;
        if (chr == "4".charAt(0)) iRet = 4;
        if (chr == "5".charAt(0)) iRet = 5;
        if (chr == "6".charAt(0)) iRet = 6;
        if (chr == "7".charAt(0)) iRet = 7;
        if (chr == "8".charAt(0)) iRet = 8;
        if (chr == "9".charAt(0)) iRet = 9;
        if (chr == "A".charAt(0)) iRet = 10;
        if (chr == "B".charAt(0)) iRet = 11;
        if (chr == "C".charAt(0)) iRet = 12;
        if (chr == "D".charAt(0)) iRet = 13;
        if (chr == "E".charAt(0)) iRet = 14;
        if (chr == "F".charAt(0)) iRet = 15;
        return iRet;
    }

    public static void main(String[] args) {
        System.out.println(AESDecoder.decrypt4Aes2Str("qdNKVvw8t9/iywW214tw1g=="));
    }

    /**
     * AES算法解密入口
     */
    public static String decrypt4Aes2Str(String contentbase64) {
        String Result = null;
        try {
            byte[] dst = decrypt4Aes(contentbase64);
            if (null != dst) {
                Result = new String(dst, "UTF-8");
            }
        } catch (Exception e3) {
        }
        return Result;
    }

    public static byte[] decrypt4Aes(String contentbase64) {
        try {
            byte[] src = base64decode(contentbase64);
            //解密
            return decryptMode(src);
        } catch (Exception e3) {
        }
        return null;

    }

    public static byte[] decryptMode(byte[] src) {
        try {
            Cipher cip = Cipher.getInstance("AES");
            cip.init(Cipher.DECRYPT_MODE, getSecretKey());
            return cip.doFinal(src);
        } catch (Exception e3) {
        }
        return null;
    }

    public static SecretKey getSecretKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
//        byte[] keybyte = getKeyByStr("6173646e636972757177656e636b6a71");// toReplace
        byte[] keybyte = getKeyByStr(PingAnEnvironment.getAseKey());
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keybyte);
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(secureRandom);
        return keygen.generateKey();
    }
}
