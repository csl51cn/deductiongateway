package org.starlightfinancial.deductiongateway.domain.goldeye;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 多期逾期联合主键类,因为表中没有主键
 * @date: Created in 2020/2/24 19:07
 * @Modified By:
 */
@Data
public class MultiOverduePrimaryKey implements Serializable {

    private static final long serialVersionUID = -4471491315356730994L;
    private Integer dateId;
    private Integer planTerm;
    private Date planTermDate;
    private BigDecimal planRepaymentPrincipal;
    private BigDecimal planRepaymentInterest;
    private BigDecimal planTotalAmount;
    private BigDecimal serplusRepaymentPrincipal;
    private BigDecimal serplusRepaymentInterest;
    private Integer overdueDays;
    private BigDecimal serplusRepaymentPenalty;
    private Integer planTypeId;
}
