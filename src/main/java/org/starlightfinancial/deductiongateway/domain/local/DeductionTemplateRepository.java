package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by senlin.deng on 2018-01-11.
 */
public interface DeductionTemplateRepository extends JpaRepository<DeductionTemplate, Integer>, JpaSpecificationExecutor<DeductionTemplate> {
}
