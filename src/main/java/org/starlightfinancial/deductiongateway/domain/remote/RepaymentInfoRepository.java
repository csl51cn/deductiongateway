package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 还款信息Repository
 * @date: Created in 2018/8/13 16:13
 * @Modified By:
 */
public interface RepaymentInfoRepository extends JpaRepository<RepaymentInfo, Long>, JpaSpecificationExecutor<RepaymentInfo> {

    /**
     * 根据传入的两个时间查询记录,[startDate,endDate)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询到的记录
     */
    List<RepaymentInfo> findByRepaymentTermDateGreaterThanEqualAndRepaymentTermDateBefore(Date startDate, Date endDate);


    /**
     * 根据生成时间用传入的两个时间查询记录,[startDate,endDate)
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     * @return 查询到的记录
     */
    List<RepaymentInfo> findByGmtCreateGreaterThanEqualAndGmtCreateLessThanEqual(Date startDate, Date endDate);

}
