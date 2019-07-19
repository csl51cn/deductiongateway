package org.starlightfinancial.deductiongateway.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: accountManage   查询参数
 * @date: Created in 2019/7/12 10:39
 * @Modified By:
 */
@Data
public class AccountManagerVO {
    /**
     * 放款开始日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loanStartDate;
    /**
     * 放款结束日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date loanEndDate;

    /**
     * 合同编号
     */
    private String contractNo;

    /**
     * 业务编号
     */
    private String bizNo;
    /**
     * 账户名
     */
    private String accountName;

}
