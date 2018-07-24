package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息的Repository接口
 * @date: Created in 2018/7/17 16:35
 * @Modified By:
 */
public interface NonDeductionRepaymentInfoRepository extends JpaRepository<NonDeductionRepaymentInfo, Long> , JpaSpecificationExecutor<NonDeductionRepaymentInfo> {
}
