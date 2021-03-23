package org.starlightfinancial.deductiongateway.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/7/6 14:57
 * @Modified By:
 */
@Data
@Builder
public class LoanIssueNotice implements Serializable {

    private static final long serialVersionUID = -8027739507772340736L;

    /**
     * 业务流水号
     */
    private Long dateId;

    /**
     * 放款表id
     */
    private Long loanIssueId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 客户身份证号
     */
    private String identityNo;

    /**
     * 卡号
     */
    private String account;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 交易时间
     */
    private Date transactionEndTime;

    /**
     * 交易合同
     */
    private String contractNo;
}
