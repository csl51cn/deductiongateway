package org.starlightfinancial.deductiongateway.faces;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
/** 注入相关的配置文件：可以写入多个配置文件 **/
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MortgageDeductionFacesTest {
    @Resource
    private MortgageDeductionFaces mortgageDeductionFaces;

    @org.junit.Test
    public void saveMortgageDeductions() throws Exception {
        mortgageDeductionFaces.saveMortgageDeductions();
    }

}