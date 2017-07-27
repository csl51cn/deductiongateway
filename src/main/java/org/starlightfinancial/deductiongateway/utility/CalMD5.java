package org.starlightfinancial.deductiongateway.utility;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 计算文件的MD5值
 */
public class CalMD5 {

    public static String getMd5ByFile(MultipartFile file)  {

        MessageDigest md = null;
        try {
            InputStream inputStream = file.getInputStream();
            md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inputStream.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (NoSuchAlgorithmException | IOException  e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        System.out.println("文件md5值：" + bigInt.toString());
        return bigInt.toString();
    }
}
