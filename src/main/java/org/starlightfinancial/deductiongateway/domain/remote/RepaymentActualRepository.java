package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 还款登记表Repository
 * @date: Created in 2018/8/28 16:52
 * @Modified By:
 */
public interface RepaymentActualRepository extends JpaRepository<RepaymentActual, Long>, JpaSpecificationExecutor<RepaymentActual> {
    /**
     * 根据dateId和还款类别查询最后一条还款登记记录
     *
     * @param dateId     业务流水号
     * @param planTypeId 还款类别
     * @return 还款登记记录
     */
    RepaymentActual findFirstByDateIdAndPlanTypeIdOrderByIdDesc(Long dateId, Integer planTypeId);

}
