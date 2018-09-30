package org.starlightfinancial.deductiongateway.domain.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.Predicate;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/30 10:37
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanIssueBasicInfoRepositoryTest {

    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Test
    public void findOne() {
        String transactionNo = "10000017820180929150400970";
        LoanIssueBasicInfo one = loanIssueBasicInfoRepository.findOne((root, query, cb) -> {
            Predicate equal = cb.equal(root.join("loanIssue").get("transactionNo"), transactionNo);
            return cb.and(equal);
        });
        System.out.println(one);

    }
}