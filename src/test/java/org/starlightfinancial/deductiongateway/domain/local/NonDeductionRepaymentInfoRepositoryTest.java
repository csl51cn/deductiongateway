package org.starlightfinancial.deductiongateway.domain.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2021/3/11 17:22
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NonDeductionRepaymentInfoRepositoryTest {

    @Autowired
    private NonDeductionRepaymentInfoRepository nonDeductionRepaymentInfoRepository;

    @Transactional
    @Test
    public void fun() {
        NonDeductionRepaymentInfo one = nonDeductionRepaymentInfoRepository.getOne(11012L);
        System.out.println(Objects.isNull(one.getOriginalId()));
    }
}
