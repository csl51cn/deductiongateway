package org.starlightfinancial.deductiongateway.faces;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
/** 注入相关的配置文件：可以写入多个配置文件 **/
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
public class MortgageDeductionControllerTest {
    @Resource
    private MortgageDeductionService mortgageDeductionController;

    @org.junit.Test
    public void saveMortgageDeductions() throws Exception {
        List<MortgageDeduction> list = new ArrayList<MortgageDeduction>();
        MortgageDeduction mortgageDeduction = getMortgageDeduction();
        list.add(mortgageDeduction);
//        mortgageDeductionController.saveMortgageDeductions(list);
    }

    private MortgageDeduction getMortgageDeduction() {
        MortgageDeduction mortgageDeduction = new MortgageDeduction();
        mortgageDeduction.setApplyMainId(0);//设置合同编号
        mortgageDeduction.setCustomerNo("");//设置客户编号
        mortgageDeduction.setCustomerName("");//设置客户名称
        mortgageDeduction.setContractNo("");//设置合同编号
        mortgageDeduction.setTarget("");//设置服务费的管理公司
        mortgageDeduction.setSplitData1(BigDecimal.valueOf(0.01));//设置分账金额1
        mortgageDeduction.setSplitData2(BigDecimal.valueOf(0.01));//设置分账金额2
        return mortgageDeduction;
    }
}