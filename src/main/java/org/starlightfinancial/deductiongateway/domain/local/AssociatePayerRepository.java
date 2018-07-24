package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/23 11:41
 * @Modified By:
 */
public interface AssociatePayerRepository extends JpaRepository<AssociatePayer,Long>,JpaSpecificationExecutor<AssociatePayer> {
}
