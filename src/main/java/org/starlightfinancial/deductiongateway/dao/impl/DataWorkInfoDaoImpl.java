package org.starlightfinancial.deductiongateway.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.starlightfinancial.deductiongateway.dao.DataWorkInfoDao;
import org.starlightfinancial.deductiongateway.domain.remote.DataWorkInfo;
import org.starlightfinancial.deductiongateway.domain.remote.DataWorkInfoRowMapper;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/12/24 14:30
 * @Modified By:
 */
@Repository
public class DataWorkInfoDaoImpl implements DataWorkInfoDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataWorkInfoDaoImpl.class);

    @Resource(name = "businessNamedJdbcTemplate")
    private NamedParameterJdbcTemplate remoteJdbcTemplate;

    @Override
    public List<DataWorkInfo> findServiceChargeCompanyByContractNoIn(Iterable<String> contractNos) {
        String sql = "SELECT " +
                " a.id, " +
                " a.Date_Id, " +
                " a.授信主体类型 , " +
                " a.授信主体编号, " +
                " a.授信金额, " +
                " a.授信期限, " +
                " a.授信期限单位, " +
                " a.放款日期, " +
                " a.还款方式, " +
                " a.利率, " +
                " a.服务费率, " +
                " a.调查评估费利率, " +
                " a.调查评估费, " +
                " a.业务编号, " +
                " a.合同编号, " +
                " a.产品类别, " +
                " a.提取结清违约金比例, " +
                " a.申请时间, " +
                " b.word AS 服务费走账公司  " +
                "FROM " +
                " Data_WorkInfo a " +
                " LEFT JOIN Dictionary b ON a.[服务费走账公司] = b.id  " +
                "WHERE " +
                " a.合同编号 IN (:contractNos)";
        Map<String, Iterable<String>> params = new HashMap<>(8);
        params.put("contractNos", contractNos);
        LOGGER.debug("Executing SQL query [{}], params: [{}]", sql, params);
        return remoteJdbcTemplate.query(sql, params,new DataWorkInfoRowMapper());
    }
}
