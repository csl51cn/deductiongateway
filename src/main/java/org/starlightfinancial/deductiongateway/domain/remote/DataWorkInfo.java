package org.starlightfinancial.deductiongateway.domain.remote;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 业务信息
 * @date: Created in 2020/7/15 10:07
 * @Modified By:
 */
@Entity(name = "data_workinfo")
@Data
public class DataWorkInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_id")
    private Long dateId;


    @Column(name = "授信主体类型")
    private Integer subjectType;


    @Column(name = "授信主体编号")
    private Integer subject;

    @Column(name = "授信金额")
    private BigDecimal approvedAmount;

    @Column(name = "授信期限")
    private Integer approvedTerms;


    @Column(name = "授信期限单位")
    private Integer termUnit;


    @Column(name = "放款日期")
    private Date loanIssueDate;


    @Column(name = "还款方式")
    private Integer repaymentMethod;


    @Column(name = "利率")
    private BigDecimal rate;


    @Column(name = "服务费率")
    private BigDecimal serviceRate;

    @Column(name = "调查评估费利率")
    private BigDecimal surveyRate;

    @Column(name = "调查评估费")
    private BigDecimal surveyFee;


    @Column(name = "业务编号")
    private String businessNo;

    @Column(name = "合同编号")
    private String contractNo;


    @Column(name = "产品类别")
    private Integer productType;


    @Column(name = "提取结清违约金比例")
    private BigDecimal damageRate;

    @Column(name = "申请时间")
    private Date applicationDate;

    @Column(name = "服务费走账公司")
    private String serviceChargeCompanyId;
}
