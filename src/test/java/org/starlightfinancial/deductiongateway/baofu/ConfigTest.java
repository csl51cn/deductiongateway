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
        String returnData = "05bccdc73cb1700bde9092734651daf0357aa94aa8bc7a8bd6dcdebe1bd89f600d3e4de87e45f75ea193fa750af66de6c6915ef540ff6c7d2cf309de22d8b44c2925a8231bbfdcaa3c535f53b68681248f95df5178168c4f062b4834b033aa5ebaa4ec56b54552ccb1b79a22a5e5d439060adb311f7d884da03e6b448f35fad46aec6ebd75805656e2db294c568c6de2d78b890e575379388737327d2508f86fab78d517b3a1b60fc4864c92e11c12a48b1446ca37af07ad5d8b218e614d97263c1deb6911fc71d24fc152537d9e2e0eb8f09a4172dfea78efe364347b452e850f6dedbc24b7f3bd74617124c1b3b9e01d51540c0556acb67d944679a909ddc20f2bd84d971661f9eb56663543ce2cba171894e2336620cef99869a4eebe155a41d27381d70b49ebfcf756fc7a0f30803d876935508b49110a4eafbfdee385bbe80ee441795e55c2eee410e48aebb001f2d12a93cb48d6025eb204a7118c20205f6b9b661cbc6450fca174e5d560540d6e430e3b26de68fbe82341015865cc9761ee918c82d7d919dc254c54aaddfc737ea9717e00334143686caea0a2e9ebd3d3eedd505486c7c6e03f51094b0d5875456956c42a099d7abdd363eb1d2501e58c313887c6b3073850518afccd0f39ff055d341413d3782ca31b0051556cad97ddd559e1490fd0fe16c908a5eb7394f6de3861b64d25e7cce132f8e25b0f3ac8";
        returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
        returnData = SecurityUtil.Base64Decode(returnData);
        System.out.println(returnData);
    }
}
