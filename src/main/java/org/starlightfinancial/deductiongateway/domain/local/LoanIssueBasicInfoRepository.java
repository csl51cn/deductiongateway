package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

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
    @EntityGraph("LoanIssueBasicInfo.loanIssues")
    Page<LoanIssueBasicInfo> findAll(Specification<LoanIssueBasicInfo> spec, Pageable pageable);

    /**
     * 根据id查询记录
     *
     * @param ids id
     * @return 返回查询到的记录
     */
    @Override
    @EntityGraph("LoanIssueBasicInfo.loanIssues")
    List<LoanIssueBasicInfo> findAll(Iterable<Long> ids);


    /**
     * 根据条件查询记录
     *
     * @param spec 条件
     * @return 查询到的记录
     */
    @EntityGraph("LoanIssueBasicInfo.loanIssues")
    @Override
    LoanIssueBasicInfo findOne(Specification<LoanIssueBasicInfo> spec);


}
