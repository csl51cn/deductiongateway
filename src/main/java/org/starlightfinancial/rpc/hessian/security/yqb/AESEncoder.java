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
 * @Description:报文加密
 * @Date: Created in 9:43 2019/6/27
 * @Modified By:
 */
public class AESEncoder {

    /**
     * @param src 1
     * @return java.lang.String
     * @Author sili.chen
     * @Description 将 s 进行 BASE64 编码
     * @Date 10:37 2019/6/27
     **/
    public static String base64encode(byte[] src) {
        if (src == null) {
            return null;
        }
        return (new sun.misc.BASE64Encoder()).encode(src);
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
        System.out.println(AESEncoder.encrypt4Aes("壹钱包test"));
    }

    /**
     * @param content 加密前数据
     * @return
     * @description: AES加密算法入口
     */
    public static String encrypt4Aes(String content) {
        try {
            byte[] src = content.getBytes("UTF-8");
            //加密
            byte[] bytOut = encryptMode(src);
            return base64encode(bytOut);
        } catch (Exception e3) {
        }
        return null;
    }

    /**
     * @param src 加密前数据字节
     * @return
     * @description: AES加密实现
     */
    public static byte[] encryptMode(byte[] src) {
        try {
            Cipher cip = Cipher.getInstance("AES");
            cip.init(Cipher.ENCRYPT_MODE, getSecretKey());
            return cip.doFinal(src);
        } catch (Exception e3) {
        }
        return null;
    }

    public static SecretKey getSecretKey() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        byte[] keybyte = getKeyByStr(PingAnEnvironment.getAseKey());
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(keybyte);
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(secureRandom);
        return keygen.generateKey();
    }
}
