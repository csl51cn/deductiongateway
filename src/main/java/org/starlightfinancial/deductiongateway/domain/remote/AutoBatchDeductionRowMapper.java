package org.starlightfinancial.deductiongateway.domain.remote;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by sili.chen on 2017/8/25
 */
public class AutoBatchDeductionRowMapper implements RowMapper {
    public static final String DATAID_COLUMN = "Date_Id";
    public static final String BUSINO_COLUMN = "业务编号";
    public static final String BANKNAME_COLUMN = "还款账号银行";
    public static final String CARDANDPASSBOOK_COLUMN = "代扣卡折类型";
    public static final String CUSTOMERNAME_COLUMN = "还款账户名";
    public static final String ACCOUT_COLUMN = "还款账号";
    public static final String CERTIFICATETYPE_COLUMN = "代扣人证件类型";
    public static final String CERTIFICATENO_COLUMN = "代扣人证件号码";
    public static final String PLANVOLUME_COLUMN = "计划期数";
    public static final String PLANDATE_COLUMN = "计划还款日";
    public static final String BXAMOUNT_COLUMN = "BX_Plan";
    public static final String FWFAMOUNT_COLUMN = "Fwf_Plan";
    public static final String FWFCOMPAMNY_COLUMN = "服务费管理司";
    public static final String CONTRACTNO_COLUMN = "合同编号";


    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        AutoBatchDeduction autoBatchDeduction = new AutoBatchDeduction();
        autoBatchDeduction.setDateId(resultSet.getInt(DATAID_COLUMN));
        autoBatchDeduction.setBusiNo(resultSet.getString(BUSINO_COLUMN));
        autoBatchDeduction.setBankName(resultSet.getString(BANKNAME_COLUMN));
        autoBatchDeduction.setCardAndPassbook(StringUtils.isEmpty(resultSet.getString(CARDANDPASSBOOK_COLUMN)) ? "卡" : resultSet.getString(CARDANDPASSBOOK_COLUMN));
        autoBatchDeduction.setCustomerName(resultSet.getString(CUSTOMERNAME_COLUMN));
        autoBatchDeduction.setAccout(resultSet.getString(ACCOUT_COLUMN));
        autoBatchDeduction.setCertificateType(resultSet.getString(CERTIFICATETYPE_COLUMN));
        autoBatchDeduction.setCertificateNo(resultSet.getString(CERTIFICATENO_COLUMN));
        autoBatchDeduction.setPlanVolume(resultSet.getString(PLANVOLUME_COLUMN));
        autoBatchDeduction.setPlanDate(resultSet.getDate(PLANDATE_COLUMN));
        autoBatchDeduction.setBxAmount(resultSet.getBigDecimal(BXAMOUNT_COLUMN));
        autoBatchDeduction.setFwfAmount(resultSet.getBigDecimal(FWFAMOUNT_COLUMN));
        autoBatchDeduction.setFwfCompamny(StringUtils.isEmpty(resultSet.getString(FWFCOMPAMNY_COLUMN)) ? "润坤" : resultSet.getString(FWFCOMPAMNY_COLUMN));
        autoBatchDeduction.setContractNo(resultSet.getString(CONTRACTNO_COLUMN));
        return autoBatchDeduction;
    }
}
