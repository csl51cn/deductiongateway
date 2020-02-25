package org.starlightfinancial.deductiongateway.domain.goldeye;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 多期逾期信息
 * @date: Created in 2020/2/19 10:02
 * @Modified By:
 */
@Data
@Entity(name = "temp_multi_overdue")
@IdClass(MultiOverduePrimaryKey.class)
public class MultiOverdue {

    @Id
    @Column(name = "date_id")
    private Integer dateId;
    @Column(name = "plan_term")
    @Id
    private Integer planTerm;
    @Column(name = "plan_term_date")
    @Id
    private Date planTermDate;
    @Column(name = "plan_repayment_principal")
    @Id
    private BigDecimal planRepaymentPrincipal;
    @Column(name = "plan_repayment_interest")
    @Id
    private BigDecimal planRepaymentInterest;
    @Column(name = "plan_total_amount")
    @Id
    private BigDecimal planTotalAmount;
    @Column(name = "serplus_repayment_principal")
    @Id
    private BigDecimal serplusRepaymentPrincipal;
    @Column(name = "serplus_repayment_interest")
    @Id
    private BigDecimal serplusRepaymentInterest;
    @Column(name = "overdue_days")
    @Id
    private Integer overdueDays;
    @Column(name = "serplus_repayment_penalty")
    @Id
    private BigDecimal serplusRepaymentPenalty;
    @Column(name = "plan_type_id")
    @Id
    private Integer planTypeId;

}
