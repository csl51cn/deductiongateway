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
     * 根据条件分页查询.
     * 注意:LoanIssueBasicInfo与LoanIssue是一对多关系,在这个接口中此方法配合@EntityGraph使用时可能会产生重复的记录,
     * 因为@EntityGraph使用的是left join 查询,如果多方有多条记录,此时查询出来的一方也会有相应的条数,例如:有一条LoanIssueBasicInfo放
     * 款基本信息记录,它对应了三次放款,使用select * from  LoanIssueBasicInfo  left join LoanIssue查询时就会查询出来三条LoanIssueBasicInfo
     * 相关的记录,而findAll返回的List中包含了三个完全一样的LoanIssueBasicInfo对象(内存地址值一样),调用者应该注意去重操作,或者通过传入的
     * Specification限定多方记录为一条.
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
    @EntityGraph("LoanIssueBasicInfo.loanIssues")
    List<LoanIssueBasicInfo> findDistinctByIdIn(Iterable<Long> ids);


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
