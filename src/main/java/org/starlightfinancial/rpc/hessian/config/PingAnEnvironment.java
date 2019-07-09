package org.starlightfinancial.rpc.hessian.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author: Senlin.Deng
 * @Description: 平安参数配置
 * @date: Created in 2019/6/28 11:56
 * @Modified By:
 */
public class PingAnEnvironment {
    private static Logger log = LoggerFactory.getLogger(PingAnEnvironment.class);
    private static String merchantId;
    private static String merchantSHA1Key;
    private static String aseKey;
    private static String transactionUrl;
    private static String merchantKey;
    private static String registrationChannel;
    private static String registrationSystem;
    private static String registrationCoOperCode;
    private static String registrationSystemId;
    private static String accessMode;
    private static String registrationUrl;


    public static void init(String fileName) throws Exception {
        Properties properties = new Properties();
        try (InputStream resourceAsStream = PingAnEnvironment.class.getClassLoader().getResourceAsStream(fileName)) {
            properties.load(resourceAsStream);
            merchantId = properties.getProperty("merchantId");
            merchantSHA1Key = properties.getProperty("merchantSHA1Key");
            aseKey = properties.getProperty("aseKey");
            transactionUrl = properties.getProperty("transactionUrl");
            merchantKey = properties.getProperty("merchantKey");
            registrationChannel = properties.getProperty("registrationChannel");
            registrationSystem = properties.getProperty("registrationSystem");
            registrationCoOperCode = properties.getProperty("registrationCoOperCode");
            registrationSystemId = properties.getProperty("registrationSystemId");
            accessMode = properties.getProperty("accessMode");
            registrationUrl = properties.getProperty("registrationUrl");
        } catch (IOException e) {
            log.error("平安参数初始化异常", e);
            throw e;
        }
    }


    public static String getMerchantId() {
        return merchantId;
    }

    public static String getMerchantSHA1Key() {
        return merchantSHA1Key;
    }

    public static String getAseKey() {
        return aseKey;
    }

    public static String getTransactionUrl() {
        return transactionUrl;
    }

    public static String getRegistrationChannel() {
        return registrationChannel;
    }

    public static String getRegistrationSystem() {
        return registrationSystem;
    }

    public static String getRegistrationCoOperCode() {
        return registrationCoOperCode;
    }

    public static String getRegistrationSystemId() {
        return registrationSystemId;
    }

    public static String getAccessMode() {
        return accessMode;
    }

    public static String getRegistrationUrl() {
        return registrationUrl;
    }

    public static String getMerchantKey() {
        return merchantKey;
    }

}
