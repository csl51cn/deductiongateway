package org.starlightfinancial.deductiongateway.async.factory;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.starlightfinancial.deductiongateway.async.AsyncManager;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;

import java.util.Arrays;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/7/6 16:57
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AsyncTaskFactoryTest {

    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Test
    public void sendLoanIssueNotice() {
        Long[] ids = {20L, 21L};
        List<LoanIssueBasicInfo> loanIssueBasicInfos = loanIssueBasicInfoRepository.findDistinctByIdIn(Arrays.asList(ids));
        AsyncManager instance = AsyncManager.getInstance();

        instance.execute(AsyncTaskFactory.sendLoanIssueNotice(loanIssueBasicInfos));

        while(true){
            if(!instance.hasWork()){
                break;
            }
        }


    }
}