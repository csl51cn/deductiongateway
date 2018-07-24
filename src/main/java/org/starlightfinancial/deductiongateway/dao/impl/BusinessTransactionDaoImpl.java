package org.starlightfinancial.deductiongateway.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.starlightfinancial.deductiongateway.dao.BusinessTransactionDao;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/20 17:19
 * @Modified By:
 */
@Repository
public class BusinessTransactionDaoImpl implements BusinessTransactionDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessTransactionDaoImpl.class);

    @Resource(name = "remoteJdbcTemplate")
    private JdbcTemplate remoteJdbcTemplate;


    /**
     * 查询所有业务信息
     *
     * @return 所有业务信息
     */
    @Override
    public List<Map<String, Object>> findAll() {
        StringBuilder stringBuilder = new StringBuilder("select a.Date_Id, a.合同编号 as  contractNo,a.服务费走账公司 as serviceFeeChargeCompany,case a.授信主体类型 when 1 then d.客户名称  when 2 THEN f.中文客户名称   end  as subject,");
        stringBuilder.append("b.姓名 as personalCoBorrowerAndGuarantor,c.企业名称  as companyCoBorrowerAndGuarantor from Data_WorkInfo  a LEFT JOIN Data_共借人担保人_个人 b on a.Date_Id = b.BusDate_Id ");
        stringBuilder.append("LEFT JOIN Data_共借人担保人_公司 c on  a.Date_Id = c.BusDate_Id left join Data_MemberInfo d on a.授信主体编号 = d.ID LEFT JOIN  Data_CompanyInfo f on  a.授信主体编号 = f.Id where a.是否放款 =485");
        LOGGER.debug("Executing SQL query [{}], params:[{}]",stringBuilder.toString(),null);
        return remoteJdbcTemplate.queryForList(stringBuilder.toString(),new Object[]{});
    }
}
