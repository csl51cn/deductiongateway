package org.starlightfinancial.deductiongateway.strategy;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/18 16:59
 * @Modified By:
 */
public interface LoanIssueStrategy {


    /**
     * 贷款发放
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     * @return 返回请求情况
     */
    Message loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos);


}
