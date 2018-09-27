package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放基本信息Repository
 * @date: Created in 2018/9/18 13:15
 * @Modified By:
 */
public interface LoanIssueBasicInfoRepository extends JpaRepository<LoanIssueBasicInfo, Long>, JpaSpecificationExecutor<LoanIssueBasicInfo> {


    /**
     * 根据条件分页查询
     *
     * @param spec     条件
     * @param pageable 分页参数
     * @return 返回查询到的记录
     */
    @Override
    @EntityGraph("LoanIssueBasicInfo.loanIssue")
    Page<LoanIssueBasicInfo> findAll(Specification<LoanIssueBasicInfo> spec, Pageable pageable);
}
