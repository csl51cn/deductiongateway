package org.starlightfinancial.deductiongateway.utility;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/8/17 16:58
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Rollback
public class AutoAccountingFtpUtilTest {
    @Test
    public void function1() {
        AutoAccountingFtpUtil autoAccountingFtpUtil = new AutoAccountingFtpUtil();

    }
}