package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 还款计划表Repository
 * @date: Created in 2018/7/23 17:12
 * @Modified By:
 */
public interface RepaymentPlanRepository extends JpaRepository<RepaymentPlan, Long>, JpaSpecificationExecutor<RepaymentPlan> {

    /**
     * 根据还款类别和还款状态查询第一条记录
     *
     * @param dateId     业务流水号
     * @param planTypeId 还款类别
     * @param status     还款状态
     * @return 还款计划信息
     */
    RepaymentPlan findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(Long dateId, Integer planTypeId, String status);

    /**
     * 根据条件查询记录数据
     *
     * @param dateId 业务流水号
     * @param status 还款状态
     * @return 符合条件的记录数
     */
    Long countByDateIdAndStatus(Long dateId, String status);

    /**
     * 根据条件查询记录数据
     *
     * @param dateId            业务流水号
     * @param isWriteOffBadLoan 是否坏账核销
     * @return 符合条件的记录数
     */
    Long countByDateIdAndIsWriteOffBadLoan(Long dateId, Integer isWriteOffBadLoan);


}
