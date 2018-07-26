package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 还款计划表Repository
 * @date: Created in 2018/7/23 17:12
 * @Modified By:
 */
public interface RepaymentPlanRepository extends JpaRepository<RepaymentPlan, Long>,JpaSpecificationExecutor<RepaymentPlan> {

    /**
     * 根据还款类别和还款状态查询最后一条记录
     * @param dateId 业务流水号
     * @param planTypeId 还款类别
     * @param status 还款状态
     * @return
     */
    RepaymentPlan findFirstByDateIdAndPlanTypeIdAndStatusOrderByIdAsc(Long dateId,Integer planTypeId, String status);
}
