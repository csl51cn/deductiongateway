package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: senlin.deng
 * @Description: 代扣卡管理Repository
 * @date: Created in 2017-08-29
 * @Modified By:
 */
public interface AccountManagerRepository extends JpaRepository<AccountManager, Integer>, JpaSpecificationExecutor<AccountManager> {

    /**
     * 根据银行卡号,合同编号,顺序查询代扣卡信息
     *
     * @param account
     * @param sort
     * @param contractNo
     * @return
     */
    AccountManager findByAccountAndSortAndContractNo(String account, int sort, String contractNo);


    /**
     * 根据dateId和银行卡号,账户名,身份证号查询代扣卡信息
     *
     * @param dateId
     * @param account
     * @param accountName
     * @param certificationNo
     * @return
     */
    AccountManager findByDateIdAndAccountAndAccountNameAndCertificateNo(Integer dateId, String account, String accountName, String certificationNo);

    /**
     * 根据合同号,银行卡号,账户名和身份证号查询代扣卡信息
     *
     * @param contractNo      合同号
     * @param account         银行卡号
     * @param accountName     账户名
     * @param certificationNo 身份证号
     * @return
     */
    AccountManager findByContractNoAndAccountAndAccountNameAndCertificateNo(String contractNo, String account, String accountName, String certificationNo);


}
