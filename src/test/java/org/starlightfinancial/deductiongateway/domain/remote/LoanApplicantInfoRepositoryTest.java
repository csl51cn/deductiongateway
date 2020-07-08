package org.starlightfinancial.deductiongateway.domain.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/7/6 15:43
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoanApplicantInfoRepositoryTest {

    @Autowired
    private   LoanApplicantInfoRepository loanApplicantInfoRepository;
    @Test
    public void findByMainDateId() {
        LoanApplicantInfo byMainDateId = loanApplicantInfoRepository.findByMainDateId(127903L);
        System.out.println(byMainDateId);
    }
}