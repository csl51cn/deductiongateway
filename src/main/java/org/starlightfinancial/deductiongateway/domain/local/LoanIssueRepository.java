package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放Repository
 * @date: Created in 2018/9/18 13:15
 * @Modified By:
 */
public interface LoanIssueRepository extends JpaRepository<LoanIssue,Long>, JpaSpecificationExecutor<LoanIssue> {
}
