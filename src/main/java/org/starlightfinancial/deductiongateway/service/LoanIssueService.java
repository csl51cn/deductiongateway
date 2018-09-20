package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/18 16:32
 * @Modified By:
 */
public interface LoanIssueService {
    /**
     * 通过接收前端传入的贷款发放基本信息进行放款操作
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    void loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos);
}
