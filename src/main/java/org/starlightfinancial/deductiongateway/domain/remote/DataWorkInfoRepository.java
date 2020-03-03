package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/3/3 13:43
 * @Modified By:
 */
public interface DataWorkInfoRepository extends JpaRepository<DataWorkInfo, Integer> {
    /**
     * 通过dateId查询记录
     *
     * @param dateId
     * @return
     */
    DataWorkInfo findByDateId(Integer dateId);
}
