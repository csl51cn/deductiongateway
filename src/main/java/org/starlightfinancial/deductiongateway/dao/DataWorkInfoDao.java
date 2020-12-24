package org.starlightfinancial.deductiongateway.dao;

import org.starlightfinancial.deductiongateway.domain.remote.DataWorkInfo;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/12/24 14:30
 * @Modified By:
 */
public interface DataWorkInfoDao {


    /**
     * 通过合同号查找服务费走账公司
     *
     * @param contractNos 合同编号集合
     * @return
     */
    List<DataWorkInfo> findServiceChargeCompanyByContractNoIn(Iterable<String> contractNos);
}
