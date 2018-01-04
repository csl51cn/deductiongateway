package org.starlightfinancial.deductiongateway.baofu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.BaofuConfig;

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
    }
}
