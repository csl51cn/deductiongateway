package org.starlightfinancial.deductiongateway.strategy;

import org.apache.commons.lang.StringUtils;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssue;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
     *
     * @param queryDate 查询日期
     * @return 请求情况
     */
    Message queryLoanIssueRefund(Date queryDate);


    /**
     * 从基本放款信息中获取到最新的LoanIssue记录:只会有一条记录是最新的
     *
     * @param loanIssueBasicInfo 基本放款信息
     * @return 最新的LoanIssue记录
     */
    default LoanIssue findTheLastRecord(LoanIssueBasicInfo loanIssueBasicInfo) {
        List<LoanIssue> loanIssues = loanIssueBasicInfo.getLoanIssues();
        List<LoanIssue> collect = loanIssues.stream().filter(loanIssue -> StringUtils.equals(loanIssue.getIsLast(), ConstantsEnum.SUCCESS.getCode()) || Objects.isNull(loanIssue.getAcceptTransactionStatus())).collect(Collectors.toList());
        return collect.get(0);
    }
}
