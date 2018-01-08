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
        String returnData = "05bccdc73cb1700bde9092734651daf0357aa94aa8bc7a8bd6dcdebe1bd89f600d3e4de87e45f75ea193fa750af66de6c6915ef540ff6c7d2cf309de22d8b44c2925a8231bbfdcaa3c535f53b68681248f95df5178168c4f062b4834b033aa5ebaa4ec56b54552ccb1b79a22a5e5d439060adb311f7d884da03e6b448f35fad471275c6d0414512f24e4f69bb5d9cfc492be2bb77519fa9d83e37d89ca12dba57d6e077ee6faaea747e838d3b146af92ecc70c05c40420872ed356d725a90e6a14607ab800560b584a3928ea1b66d8b3108a889fadfc70984e43368ed581d1c064c0bf66533f123bba223f4bbd33ee50081fc773ae6e9d7e3eae4326745caae94825d132ed1e86f7187ebb84cf2772f896c3f8021cca580d656821d9cd2ca9cd2156c92307594c0872dbbb2e2b0d247f90c8ff919fdbff772d3e6778c76da1ae0b5d1ae59aa81ddd631ac9290018577a3737c9cde9f87dcc4e78ec5b6e0fda740e963af024f6f05ef5435a694af7d2704dc9d14f272dbb0a512c3c6853addf192b39a93922d528829c5e78a219097178e3a362c51636e70a1b3bc1828a3389f248772b9a455f51e90a3f09a82e5f4fcf4262c6fe95aabae2fcfa9a99db734e3e42dfb23efafdb498bf398dbf5408a6b2c0873ab51614db9bf76aa31a7ea0be7ad4152e4bb9c8ebe83d9b15bc49a44aeb8cadb20d85cbbd4b711f9998d1ff9be2";
        returnData = RsaCodingUtil.decryptByPubCerFile(returnData, baofuConfig.getCerFile());
        returnData = SecurityUtil.Base64Decode(returnData);
        System.out.println(returnData);
    }
}
