package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * 卡号管理实体类RowMapper
 *
 * @author senlin.deng
 */
public class AccountManagerRowMapper implements RowMapper<AccountManager> {

    public static final String DATAID_COLUMN = "dateid";
    public static final String BIZNO_COLUMN = "bizno";
    public static final String CONTRACTNO_COLUMN = "contractno";
    public static final String BANKNAME_COLUMN = "bankname";
    public static final String ACCOUNTNAME_COLUMN = "accountname";
    public static final String ACCOUT_COLUMN = "account";
    public static final String CERTIFICATETYPE_COLUMN = "certificatetype";
    public static final String CERTIFICATENO_COLUMN = "certificateno";
    public static final String SORT_COLUMN = "sort";
    public static final String LOANDATE_COLUMN = "loandate";
    public static final String MOBILE_COLUMN = "mobile";
    public static final String CUSTOMERID_COLUMN  = "customerid";




    @Override
    public AccountManager mapRow(ResultSet resultSet, int i) throws SQLException {
        AccountManager accountManager = new AccountManager();
        accountManager.setDateId(resultSet.getInt(DATAID_COLUMN));
        accountManager.setBizNo(resultSet.getString(BIZNO_COLUMN));
        accountManager.setContractNo(resultSet.getString(CONTRACTNO_COLUMN));
        accountManager.setBankName(resultSet.getString(BANKNAME_COLUMN));
        accountManager.setAccountName(resultSet.getString(ACCOUNTNAME_COLUMN));
        accountManager.setAccount(resultSet.getString(ACCOUT_COLUMN));
        accountManager.setCertificateType(resultSet.getString(CERTIFICATETYPE_COLUMN));
        accountManager.setCertificateNo(resultSet.getString(CERTIFICATENO_COLUMN));
        accountManager.setSort(resultSet.getInt(SORT_COLUMN));
        accountManager.setChangeTime(new Date());
        accountManager.setLoanDate(resultSet.getDate(LOANDATE_COLUMN));
        accountManager.setMobile(resultSet.getString(MOBILE_COLUMN));
        accountManager.setCustomerId(resultSet.getString(CUSTOMERID_COLUMN));
        accountManager.setCreateTime(new Date());
        return accountManager;
    }
}
