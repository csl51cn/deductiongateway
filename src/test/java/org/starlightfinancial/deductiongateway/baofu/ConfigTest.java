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
        String returnData = "05bccdc73cb1700bde9092734651daf0357aa94aa8bc7a8bd6dcdebe1bd89f600d3e4de87e45f75ea193fa750af66de6c6915ef540ff6c7d2cf309de22d8b44c2925a8231bbfdcaa3c535f53b68681248f95df5178168c4f062b4834b033aa5ebaa4ec56b54552ccb1b79a22a5e5d439060adb311f7d884da03e6b448f35fad42d97609ee5b70d81a6a78dd2a80eb56350d6948ba31b50f1af0dc78a994b06b7e283bac9bdd824671fdf16b4a420fa7afcd1804890d84b3e7ee2e6bb16a996f856e679b557300df8777b052245967fd9e8184d303b5d33295ff151b9d196e550c1fb4cf4c3f186b16e36916642d5d7851664b8c393da747bfefe3067c24378ed37dfd069683042270e3d4df270f4fc2bf0781722a8a70bbe26aad63459bb9120d61db117f45c8a1403ea4701834ec6c819b6707b32c716d78c79e2d80470a4845ef1a397cfbe58589590ee9f202157507e74dc60ec5a26e21c508c173df24fc832d561748bfa75d268a9bb4de4f23061ed44cbf11ed1e80cbb58700affefe4ec61e90d73ee8b86a07228daa8821344c9b2fcd93e7071c34326e1fa843107b2c7885f24df4c9c8c437a4b6f938c77e50229689676f2bb805f1de502140cc4294bbd1e7b744278cf7cf1a6d966bbf0625a5d807ded0fa331d438eaaf525d2c1b1247ad6977271c79d86c9b4e498b9da9c1451c222a460a17e059827d4285c9631979f26a333aeed12130b8cce32b52b647a6e275f2384db631fe302e6606c32b2ea08801933935cb0007159c055517783ef82af9802be58927c1cfbf769168ec9ebfba7c163e548ac734eca78e27219e4193f4f2f40aa005d79997e40d06740cfd9d21408cd32820ef09164f1d142f99510411d1fce127976b2f26907d41358edb";
        returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
        returnData = SecurityUtil.Base64Decode(returnData);
        System.out.println(returnData);
    }
}
