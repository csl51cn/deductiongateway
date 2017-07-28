package org.starlightfinancial.deductiongateway.faces;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.MortgageDeductionRepository;
import org.starlightfinancial.deductiongateway.domain.SysDictRepository;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;

import javax.transaction.Transactional;
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
