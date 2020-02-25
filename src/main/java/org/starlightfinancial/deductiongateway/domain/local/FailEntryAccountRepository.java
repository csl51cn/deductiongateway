package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/24 12:58
 * @Modified By:
 */
public interface FailEntryAccountRepository extends JpaRepository<FailEntryAccount, Integer> {

    /**
     * 通过条件查询记录
     *
     * @param contractNo 合同号
     * @param date       生成日期
     * @return
     */
    FailEntryAccount findByContractNoAndGmtCreateGreaterThanEqual(String contractNo, Date date);
}
