package org.starlightfinancial.deductiongateway.domain.remote;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/12/24 15:02
 * @Modified By:
 */
public class DataWorkInfoRowMapper implements RowMapper<DataWorkInfo> {
    /**
     * Implementations must implement this method to map each row of data
     * in the ResultSet. This method should not call {@code next()} on
     * the ResultSet; it is only supposed to map values of the current row.
     *
     * @param rs     the ResultSet to map (pre-initialized for the current row)
     * @param rowNum the number of the current row
     * @return the result object for the current row (may be {@code null})
     * @throws SQLException if a SQLException is encountered getting
     *                      column values (that is, there's no need to catch SQLException)
     */
    @Override
    public DataWorkInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataWorkInfo dataWorkInfo = new DataWorkInfo();
        dataWorkInfo.setId(rs.getLong("id"));
        dataWorkInfo.setDateId(rs.getLong("date_id"));
        dataWorkInfo.setSubjectType(rs.getInt("授信主体类型"));
        dataWorkInfo.setSubject(rs.getInt("授信主体编号"));
        dataWorkInfo.setApprovedAmount(rs.getBigDecimal("授信金额"));
        dataWorkInfo.setApprovedTerms(rs.getInt("授信期限"));
        dataWorkInfo.setTermUnit(rs.getInt("授信期限单位"));
        dataWorkInfo.setLoanIssueDate(rs.getDate("放款日期"));
        dataWorkInfo.setRepaymentMethod(rs.getInt("还款方式"));
        dataWorkInfo.setRate(rs.getBigDecimal("利率"));
        dataWorkInfo.setServiceRate(rs.getBigDecimal("服务费率"));
        dataWorkInfo.setSurveyRate(rs.getBigDecimal("调查评估费利率"));
        dataWorkInfo.setSurveyFee(rs.getBigDecimal("调查评估费"));
        dataWorkInfo.setBusinessNo(rs.getString("业务编号"));
        dataWorkInfo.setContractNo(rs.getString("合同编号"));
        dataWorkInfo.setProductType(rs.getInt("产品类别"));
        dataWorkInfo.setDamageRate(rs.getBigDecimal("提取结清违约金比例"));
        dataWorkInfo.setApplicationDate(rs.getDate("申请时间"));
        dataWorkInfo.setServiceChargeCompanyId(rs.getString("服务费走账公司"));
        return dataWorkInfo;
    }
}
