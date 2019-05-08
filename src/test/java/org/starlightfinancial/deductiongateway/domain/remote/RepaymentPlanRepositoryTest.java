package org.starlightfinancial.deductiongateway.domain.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/12 17:27
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RepaymentPlanRepositoryTest {

    @Autowired
    private  RepaymentPlanRepository repaymentPlanRepository;
    @Test
    public void countByDateIdAndIsWriteOffBadLoan() {
        Long isWriteOffBadLoan = repaymentPlanRepository.countByDateIdAndIsWriteOffBadLoan(101421L, 1);
        System.out.println(isWriteOffBadLoan);
    }
}