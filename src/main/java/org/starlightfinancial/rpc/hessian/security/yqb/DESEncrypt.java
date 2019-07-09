package org.starlightfinancial.rpc.hessian.security.yqb;

import org.apache.commons.codec.binary.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.security.Key;

/**
 * @Author: SiliChen
 * @Description: 加密
 * @Date: Created in 10:40 2019/6/27
 * @Modified By:
 */
public class DESEncrypt {

    /**
     * 密钥算法
     */
    public static final String KEY_ALGORITHM = "DESede";

    /**
     * 加密入口
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String encrypt(String data, String key) throws Exception {
        byte[] crypt = encrypt(data.getBytes(), key.getBytes());
        return new String(Hex.encodeHex(crypt)).toUpperCase();
    }

    /**
     * 解密入口
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String data, String key) throws Exception {
        byte[] bSrc = Hex.decodeHex(data.toCharArray());
        byte[] crypt = decrypt(bSrc, key.getBytes());
        return new String(crypt);
    }

    /**
     * 转换密钥
     *
     * @param key 二进制密钥
     * @return KEY 密钥
     * @throws Exception
     */
    private static Key toKey(byte[] key) throws Exception {
        DESedeKeySpec dks = new DESedeKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(KEY_ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * 解密
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return byte[]   解密数据
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, k);
        return cipher.doFinal(data);
    }

    /**
     * 加密
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return byte[]   加密数据
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        Key k = toKey(key);
        Cipher cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, k);
        return cipher.doFinal(data);
    }
}
