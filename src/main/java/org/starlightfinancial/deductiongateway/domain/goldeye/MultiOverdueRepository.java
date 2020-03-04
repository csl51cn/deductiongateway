package org.starlightfinancial.deductiongateway.domain.goldeye;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/19 10:41
 * @Modified By:
 */
public interface MultiOverdueRepository extends JpaRepository<MultiOverdue, Integer> {

    /**
     * 通过dateId,计划还款日期,逾期天数查询数据
     *
     * @param dateId     流水号
     * @param date       计划还款日期
     * @param overdueDay 逾期天数
     * @return
     */
    List<MultiOverdue> findByDateIdAndPlanTermDateLessThanEqualAndOverdueDaysGreaterThanOrderByPlanTermDateAsc(Integer dateId, Date date, Integer overdueDay);

    /**
     * 通过dateId,还款类别,计划还款日期,逾期天数查询数据
     *
     * @param dateId     流水号
     * @param planTypeId 还款类别
     * @param date       计划还款日期
     * @param overdueDay 逾期天数
     * @return
     */
    List<MultiOverdue> findByDateIdAndPlanTypeIdAndPlanTermDateLessThanEqualAndOverdueDaysGreaterThanOrderByPlanTermDateAsc(Integer dateId, Integer planTypeId, Date date, Integer overdueDay);
}
