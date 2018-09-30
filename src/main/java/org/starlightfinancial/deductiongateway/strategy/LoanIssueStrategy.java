package org.starlightfinancial.deductiongateway.strategy;

import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;

import java.util.Date;
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


    /**
     * 查询贷款发放结果
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     * @return 返回请求情况
     */
    Message queryLoanIssueResult(List<LoanIssueBasicInfo> loanIssueBasicInfos);


    /**
     * 查询代付交易退款结果
     * @param queryDate 查询日期
     * @return 请求情况
     */
    Message queryLoanIssueRefund(Date queryDate);
}
