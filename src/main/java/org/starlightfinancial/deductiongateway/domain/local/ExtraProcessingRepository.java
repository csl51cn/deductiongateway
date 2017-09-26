package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by senlin.deng on 2017-08-29.
 */
public interface ExtraProcessingRepository extends JpaRepository<ExtraProcessing,Integer> ,JpaSpecificationExecutor<ExtraProcessing> {
    List<ExtraProcessing> findByIdIn(Integer[] ids);
}
