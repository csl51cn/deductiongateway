package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.dao.BusinessTransactionDao;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.service.BusinessTransactionService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/20 15:28
 * @Modified By:
 */
@Service("businessTransactionService")
public class BusinessTransactionServiceImpl implements BusinessTransactionService {

    @Autowired
    private BusinessTransactionDao businessTransactionDao;


    /**
     * 查询所有业务信息
     *
     * @return 所有业务信息
     */
    @Override
    public Map<String, BusinessTransaction> findAll() {
        //查找所有放款业务信息
        List<Map<String, Object>> allData = businessTransactionDao.findAll();
        //为使用并行流,将List转换为线程安全的List
        List<Map<String, Object>> synchronizedAllData = Collections.synchronizedList(allData);
        //合同号-BusinessTransaction映射的Map
        ConcurrentHashMap<String, BusinessTransaction> concurrentHashMap = new ConcurrentHashMap<>(9500);
        //使用并行流处理原始的放款业务信息,如果同一个合同号有多条数据,合并成到一个BusinessTransaction对象中
        synchronizedAllData.parallelStream().forEach(map -> {
            //通过合同编号获取BusinessTransaction对象
            BusinessTransaction businessTransaction = concurrentHashMap.getOrDefault(map.get("contractNo"), new BusinessTransaction());
            if (StringUtils.isBlank(businessTransaction.getContractNo())) {
                //如果businessTransaction 的 合同号为null,需要设置合同号,dateId,subject 三个属性,并将这个对象放进concurrentHashMap,以合同编号为键
                businessTransaction.setContractNo((String) map.get("contractNo"));
                businessTransaction.setDateId(Long.valueOf((Integer) map.get("Date_Id")));
                businessTransaction.setSubject((String) map.get("subject"));
                businessTransaction.setServiceFeeChargeCompany((String) map.get("serviceFeeChargeCompany"));
                concurrentHashMap.put(businessTransaction.getContractNo(), businessTransaction);
            }
            //添加个人共借人和担保人
            businessTransaction.getPersonalCoBorrowerAndGuarantor().add((String) map.get("personalCoBorrowerAndGuarantor"));
            //添加企业共借人和担保人
            businessTransaction.getCompanyCoBorrowerAndGuarantor().add((String) map.get("companyCoBorrowerAndGuarantor"));
        });
        return concurrentHashMap;
    }
}
