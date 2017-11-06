package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by senlin.deng on 2017-10-10.
 */
public interface HolidayRepository extends JpaRepository<Holiday, Integer> {

    Holiday findByNonOverTime(String now);
}
