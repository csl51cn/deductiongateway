package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息的Repository接口
 * @date: Created in 2018/7/17 16:35
 * @Modified By:
 */
public interface NonDeductionRepaymentInfoRepository extends JpaRepository<NonDeductionRepaymentInfo, Long>, JpaSpecificationExecutor<NonDeductionRepaymentInfo> {
    /**
     * 根据条件查询记录
     *
     * @param startDate    开始日期(包含)
     * @param endDate      结束日期(包含)
     * @param isIntegrated 信息是否完整
     * @return 返回查询到的记录
     */
    List<NonDeductionRepaymentInfo> findByRepaymentTermDateBetweenAndIsIntegrated(Date startDate, Date endDate, String isIntegrated);

    /**
     * 根据条件查询记录
     *
     * @param startDate    开始日期(包含)
     * @param endDate      结束日期(不含)
     * @return 返回查询到的记录
     */
    List<NonDeductionRepaymentInfo> findByRepaymentTermDateGreaterThanEqualAndRepaymentTermDateBefore(Date startDate, Date endDate);

    /**
     * 根据生成时间查询记录
     *
     * @param startDate    开始日期(包含)
     * @param endDate      结束日期(不含)
     * @return 返回查询到的记录
     */
    List<NonDeductionRepaymentInfo> findByGmtCreateGreaterThanEqualAndGmtCreateLessThanEqual(Date startDate, Date endDate);


}
