package org.starlightfinancial.deductiongateway.domain.remote;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 豁免信息表实体类
 * @date: Created in 2020/2/20 16:03
 * @Modified By:
 */
@Data
@Entity(name = "date_豁免申请表")
public class ExemptInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "date_id")
    private Integer dateId;

    @Column(name = "期数")
    private Integer planTerm;
    @Column(name = "申请还款日期")
    private Date applyRepaymentDate;

    @Column(name = "豁免类型")
    private Integer exemptType;

    @Column(name = "豁免本金")
    private Float exemptPrincipal;

    @Column(name = "豁免利息")
    private Float exemptInterest;

    @Column(name = "豁免罚息")
    private Float exemptPenalty;
    @Column(name = "豁免违约金")
    private Float exemptDamage;
    @Column(name = "应还本金")
    private Float planPrincipal;

    @Column(name = "应还利息")
    private Float planInterest;
    @Column(name = "应还罚息")
    private Float planPenalty;
    @Column(name = "应还违约金")
    private Float planDamage;
    @Column(name = "申请理由")
    private String reason;
    @Column(name = "提交时间")
    private Date applyDate;

    @Column(name = "提交人编号")
    private Integer submitterNo;

    @Column(name = "提交人")
    private String submitter;
    @Column(name = "初审意见")
    private String initialReviewResult;
    @Column(name = "初审时间")
    private Date initialReviewDate;
    @Column(name = "初审人编号")
    private Integer initialReviewerNo;
    @Column(name = "初审人")
    private String initialReviewer;


    @Column(name = "终审意见")
    private String finalReviewResult;
    @Column(name = "终审时间")
    private Date finalReviewDate;
    @Column(name = "终审人编号")
    private Integer finalReviewerNo;
    @Column(name = "终审人")
    private String finalReviewer;

    /**
     * 0 提交 1初审通过 2 初审拒绝 3终审拒绝 99终审通过
     */
    @Column(name = "状态")
    private Integer applyStatus;

    @Column(name = "还款计划类别")
    private Integer planTypeId;

    @Column(name = "豁免项目")
    private Integer exemptItem;

    @Column(name = "金额类型")
    private Integer amountType;

    @Column(name = "发起部门")
    private String department;

    @Column(name = "授权人")
    private String authorizer;

    @Column(name = "操作人")
    private String operator;

    @Column(name = "备注")
    private String remark;

    @Column(name = "合同编号")
    private String contractNo;

    @Column(name = "客户名称")
    private String customerName;

    /**
     * 1-已使用,0-未使用
     */
    @Column(name = "是否使用")
    private Integer usedStatus;


}
