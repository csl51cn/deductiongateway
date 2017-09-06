package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by senlin.deng on 2017-08-28.
 */
public interface LimitManagerRepository extends JpaRepository<LimitManager, Integer> ,JpaSpecificationExecutor<LimitManager>{

    LimitManager findByBankName(String bankName);
