package org.starlightfinancial.deductiongateway.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by sili.chen on 2017/7/26
 */
public interface MortgageDeductionRepository extends JpaRepository<MortgageDeduction, Integer> {
    MortgageDeduction findByOrdId(String ordId);

    List<MortgageDeduction> findByTypeAndCreatId(String type, int creatId);
}
