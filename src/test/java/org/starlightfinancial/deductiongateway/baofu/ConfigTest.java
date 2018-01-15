package org.starlightfinancial.deductiongateway.baofu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;

import javax.transaction.Transactional;

/**
 * Created by sili.chen on 2018/1/2
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
@Rollback
public class ConfigTest {
    @Autowired
    BaofuConfig baofuConfig;

    @Test
    public void test() throws Exception {
        System.out.println("version" + baofuConfig.getVersion());
        System.out.println(baofuConfig.getMemberId());
        System.out.println(baofuConfig.getCerFile());
        String returnData = "7927f6adf86427202e60a45cb7e6b334be04a52942c02d6ebc60d542eb8c782ee3c6dc6de0e70adc7918198beec37a60dce70d6edfdb2b3a50838f89839bcb66960a2199c5b9c28f5d386ad1fc7cfc9976f6f573d63f54f324611135d06997048fe9a7b0f2da04eb0dc23fe3ff5a1d6bbea4d17d0b4fa134cc54cef8749efd9c699a53c3d240cbea2a8d84412a0b14ddeda92db2a9bcfb258cfe3fc6983974b4f600e2ebe3968bdd3c5135e89fe03ba88d71e98bc62345b584101d07d56baabdf5ca699e28f5a5a97f2600e8ed7f3b064d0994753cb73da6bef428f09b64d5a415cf147856b0f08077f593e4ff43e16de2d8f891699b0448aafd4e0f9a0c5b18";
        returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
        returnData = SecurityUtil.Base64Decode(returnData);
        System.out.println(returnData);
    }
}
