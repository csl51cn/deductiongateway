package org.starlightfinancial.deductiongateway.strategy.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;
import org.starlightfinancial.deductiongateway.strategy.LoanIssueStrategy;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/21 16:17
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BaoFuLoanIssueStrategyImplTest {
    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Resource(name = "1001")
    private LoanIssueStrategy  loanIssueStrategy;
    @Test
    @Transactional(rollbackFor = Exception.class)
    public void loanIssue() {

        List<LoanIssueBasicInfo> all = loanIssueBasicInfoRepository.findAll(Arrays.asList(1L, 2L));
        loanIssueStrategy.loanIssue(all);




    }
}