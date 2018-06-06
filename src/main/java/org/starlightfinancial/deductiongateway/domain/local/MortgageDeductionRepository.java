package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by sili.chen on 2017/7/26
 */
public interface MortgageDeductionRepository extends JpaRepository<MortgageDeduction, Integer>, JpaSpecificationExecutor<MortgageDeduction> {
    MortgageDeduction findByOrdId(String ordId);

    List<MortgageDeduction> findByTypeAndCreatId(String type, int creatId);

    List<MortgageDeduction> findByIdIn(Integer[] ids);

    @Modifying
    @Query("update BU_MORTGAGEDEUCTION t set t.checkState = ?1 where OrdId = ?2")
    int setCheckStateFor(String checkState, String ordId);


    @Modifying
    @Query("update BU_MORTGAGEDEUCTION t set t.ledgerState = ?1 where OrdId = ?2")
    int setLedgerStateFor(String ledgerState, String ordId);

    MortgageDeduction findById(Integer id);

}
