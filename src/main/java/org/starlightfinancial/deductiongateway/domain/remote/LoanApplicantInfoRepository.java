package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/7/6 15:32
 * @Modified By:
 */
public interface LoanApplicantInfoRepository extends JpaRepository<LoanApplicantInfo, Long> {

    /**
     * 通过maindate_id查询记录
     *
     * @param dateId
     * @return
     */
    LoanApplicantInfo findByMainDateId(Long dateId);
}
