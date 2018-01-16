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
        String returnData = "05bccdc73cb1700bde9092734651daf0357aa94aa8bc7a8bd6dcdebe1bd89f600d3e4de87e45f75ea193fa750af66de6c6915ef540ff6c7d2cf309de22d8b44c2925a8231bbfdcaa3c535f53b68681248f95df5178168c4f062b4834b033aa5ebaa4ec56b54552ccb1b79a22a5e5d439060adb311f7d884da03e6b448f35fad4abfa05de5abaf7a58876356e4acd6dbca115d0fe035e9e129044a9d0deffe50c9333d30d4d8bff96aa758e7e4a86e389152fd3bb95d982f71bca8a57c726129936bf15f754829639c0071c1d91cb5e23ce7ddf19092224773fa00a99c236e337dfb1e6af3048492326f344f8fa25069086387bdfa0ec594a791cfa9d9cd9531e3d6cf0e51e9fddc4d48a2738c6a6f337d5cf8b8ea4792cc47aba36b6c3b9be83203217fef966022ba7fc6d4f0cf324e82c8f4842a89c7057580857777f10dfeb8e884f54d987fa0260b8c53188f37547767e390471ede2a1dc0137fd7d65781fff6974bf472e11c4873733fb0fabf46832c475c681cf69c58cb0cd1ecba9c46214bb6982bf63331053a60dbad222e84de2aa38c1aace484f2cb7f7114eb9925a551ecaf5afd07942fe39a419f7245c6345ad2ac866271724885a74439bb491dc5746b2cfbd11fc10b817768e064a88d35cfbf5da09cf3612b112a51f4767ff32f692935a288bb40318146d15a5dae6ad5fc5b3055c33674ee78ad652c789e4ab";
        returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
        returnData = SecurityUtil.Base64Decode(returnData);
        System.out.println(returnData);
    }
}
