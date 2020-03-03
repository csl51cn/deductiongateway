package org.starlightfinancial.deductiongateway.domain.remote;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/3/3 13:15
 * @Modified By:
 */
@Data
@Entity(name = "data_workinfo")
public class DataWorkInfo {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "date_id")
    private Integer dateId;
    @Column(name = "memberid")
    private Integer memberId;

    @Column(name = "companyid")
    private Integer ompanyId;

    @Column(name = "授信主体类型")
    private Integer subjectType;
    @Column(name = "授信主体编号")
    private Integer subjectId;
    @Column(name = "授信金额")
    private Float amount;

    @Column(name = "授信期限")
    private Integer term;

    @Column(name = "授信期限单位")
    private Integer termUnit;

    @Column(name = "计划放款日期")
    private Date planLoanDate;
    @Column(name = "放款日期")
    private Date loanDate;

    @Column(name = "还款方式")
    private Integer repaymentMethod;

    @Column(name = "其他软信息")
    private String otherInfo;

    @Column(name = "利率")
    private Float rate;

    @Column(name = "服务费率")
    private Float serviceRate;
    @Column(name = "调查评估费利率")
    private Float surveyRate;
    @Column(name = "调查评估费")
    private Float surveyFee;

    @Column(name = "费用利率")
    private  Float  feeRate;



}
