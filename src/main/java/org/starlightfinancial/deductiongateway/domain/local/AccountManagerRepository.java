package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by senlin.deng on 2017-08-29.
 */
public interface AccountManagerRepository extends JpaRepository<AccountManager, Integer>, JpaSpecificationExecutor<AccountManager> {

    AccountManager findByAccountAndSortAndContractNo(String account, int sort, String contractNo);
}
