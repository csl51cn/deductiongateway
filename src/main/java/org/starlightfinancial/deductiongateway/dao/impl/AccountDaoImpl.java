package org.starlightfinancial.deductiongateway.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.starlightfinancial.deductiongateway.dao.AccountDao;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRowMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author: senlin.deng
 * @Description:
 * @date: Created in 2018/5/7 13:40
 * @Modified By:
 */
@Repository
public class AccountDaoImpl implements AccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDaoImpl.class);

    @Resource(name = "remoteJdbcTemplate")
    private JdbcTemplate remoteJdbcTemplate;


    @Override
    public Integer findDateIdByBizNo(String bizNo) {
        String sql = "select Date_id from  Data_WorkInfo where 业务编号 = ?";
        LOGGER.debug("Executing SQL query [{}], params: [{}]", sql, bizNo);
        Integer dateId = remoteJdbcTemplate.queryForObject(sql, new Object[]{bizNo} ,Integer.class);
        return dateId;
    }

    @Override
    public List<AccountManager> findAccountByDateId(Integer dateId) {
        String sql = "SELECT " +
                "  a.member as customerid," +
                " a.date_id AS dateid, " +
                " d.Content AS contractno, " +
                " e.ProductSerNum AS bizno, " +
                " '身份证' AS certificatetype, " +
                " b.客户名称 AS accountName, " +
                " b.身份证号码 AS certificateno, " +
                " c.content AS loandate, " +
                " g.content AS account, " +
                " i.Word AS bankname, " +
                " f.Content AS mobile, " +
                "( " +
                " CASE a.form_arrno " +
                " WHEN 8 THEN " +
                "  1 " +
                " WHEN 34 THEN " +
                "  2 " +
                " WHEN 45 THEN " +
                "  3 " +
                " WHEN 56 THEN " +
                "  4 " +
                " WHEN 67 THEN " +
                "  5 " +
                " ELSE " +
                "  5 " +
                " END " +
                ") AS sort "+
                "FROM " +
                " WorkData_Member a " +
                "LEFT JOIN Data_MemberInfo b ON b.id = a.member " +
                "LEFT JOIN WorkData_Date c ON c.date_id = a.date_id " +
                "AND c.GoBackId = 0 " +
                "AND c.Form_Id = 1091 " +
                "AND c.form_arrno = 2 " +
                "LEFT JOIN WorkData_Text d ON d.date_id = a.date_id " +
                "AND d.GoBackId = 0 " +
                "AND d.Form_Id = 1091 " +
                "AND d.form_arrno = 13 " +
                "LEFT JOIN Data_VoteImportInfo e ON e.Date_Id = a.Date_Id " +
                "LEFT JOIN WorkData_Text f ON f.Date_Id = a.date_id " +
                "AND f.GoBackId = 0 " +
                "AND f.Form_Id = 1091 " +
                "LEFT JOIN WorkData_Text g ON g.date_id = a.date_id " +
                "AND g.GoBackId = 0 " +
                "AND g.Form_Id = 1091 " +
                "LEFT JOIN WorkData_Dictionary h ON h.date_id = a.date_id " +
                "AND h.GoBackId = 0 " +
                "AND h.form_id = 1091 " +
                "LEFT JOIN Dictionary i ON i.id = h.content " +
                "WHERE " +
                " a.form_id = 1091 " +
                "AND ( " +
                " ( " +
                "  a.form_arrno = 8 " +
                "  AND g.Form_Arrno = 11 " +
                "  AND h.form_arrno = 9 " +
                "  AND f.Form_Arrno = 31 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 34 " +
                "  AND g.Form_Arrno = 40 " +
                "  AND h.form_arrno = 42 " +
                "  AND f.Form_Arrno = 44 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 45 " +
                "  AND g.Form_Arrno = 51 " +
                "  AND h.form_arrno = 53 " +
                "  AND f.Form_Arrno = 55 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 56 " +
                "  AND g.Form_Arrno = 62 " +
                "  AND h.form_arrno = 64 " +
                "  AND f.Form_Arrno = 66 " +
                " ) " +
                " OR ( " +
                "  a.form_arrno = 67 " +
                "  AND g.Form_Arrno = 73 " +
                "  AND h.form_arrno = 75 " +
                "  AND f.Form_Arrno = 77 " +
                " ) " +
                ") " +
                "AND a.GoBackId = 0 " +
                "AND a.date_id = ?";
        LOGGER.debug("Executing SQL query [{}], params: [{}]", sql, dateId);
        List<AccountManager> accountManagerList = remoteJdbcTemplate.query(sql, new Object[]{dateId}, new AccountManagerRowMapper());
        return accountManagerList;
    }
}
