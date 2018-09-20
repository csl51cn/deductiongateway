package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放基本信息Repository
 * @date: Created in 2018/9/18 13:15
 * @Modified By:
 */
public interface LoanIssueBasicInfoRepository extends JpaRepository<LoanIssueBasicInfo, Long>, JpaSpecificationExecutor<LoanIssueBasicInfo> {
}
