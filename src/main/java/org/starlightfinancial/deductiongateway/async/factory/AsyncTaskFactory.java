package org.starlightfinancial.deductiongateway.async.factory;

import lombok.extern.slf4j.Slf4j;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 异步任务工厂类
 * @date: Created in 2019/1/14 14:06
 * @Modified By:
 */
@Slf4j
public class AsyncTaskFactory {


    public static Runnable sendLoanIssueNotice(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        return () -> {
            LoanIssueService loanIssueService = SpringContextUtil.getBean(LoanIssueService.class);
            loanIssueService.sendLoanIssueNotice(loanIssueBasicInfos);
        };
    }

}
