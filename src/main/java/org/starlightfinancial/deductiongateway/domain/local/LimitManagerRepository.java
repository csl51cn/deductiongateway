package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by senlin.deng on 2017-08-28.
 */
public interface LimitManagerRepository extends JpaRepository<LimitManager, Integer> {

    LimitManager findByBankName(String bankName);
}
