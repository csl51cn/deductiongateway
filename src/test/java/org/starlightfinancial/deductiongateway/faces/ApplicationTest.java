package org.starlightfinancial.deductiongateway.faces;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.domain.local.SysDictRepository;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.transaction.Transactional;
import java.net.URL;
import java.util.List;


/**
 * Created by sili.chen on 2017/7/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
@Rollback(value = false)
public class ApplicationTest {
    @Autowired
    private MortgageDeductionRepository mortgageDeductionRepository;

    @Autowired
    private SysDictRepository sysDictRepository;

    @Autowired
    private MortgageDeductionService mortgageDeductionService;

    @Before
    public void setUp() {
    }

    @Test
    public void test() throws Exception {
        URL url = ResourceUtils.getURL("classpath:" + Utility.SEND_BANK_KEY_FILE);
        System.out.println("urlxxxxx    " + url.getPath());

//        MortgageDeduction mortgageDeduction = mortgageDeductionRepository.findByOrdId("789");
//        System.out.println(mortgageDeduction.getParam1());

//        List<SysDict> list = sysDictRepository.findByDicType("726");
//        System.out.println(list.size());

//        File file = new File("src/main/resources/代扣粘贴模版.xls");
//        mortgageDeductionService.importCustomerData(file, 14);
        List<MortgageDeduction> list = mortgageDeductionRepository.findByTypeAndCreatId("1", 14);
        mortgageDeductionService.saveMortgageDeductions(list);

    }
}
