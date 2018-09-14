package org.starlightfinancial.deductiongateway.domain.remote;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 还款计划表
 * @date: Created in 2018/7/6 11:35
 * @Modified By:
 */
@Entity(name = "Date_还款计划表")
public class RepaymentPlan {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 业务dateId
     */
    @Column(name = "Date_Id")
    private Long dateId;

    /**
     * 总期数
     */
    @Column(name = "总期数")
    private Integer totalNumberOfPeriods;


    /**
     * 计划还款期数
     */
    @Column(name = "计划期数")
    private Integer planTerm;

    /**
     * 当期计划还款日
     */
    @Column(name = "计划还款日")
    private Date planTermDate;

    /**
     * 当期应还本金
     */
    @Column(name = "计划应还本金")
    private Float planRepaymentPrincipal;

    /**
     * 当期应还利息
     */
    @Column(name = "计划应还利息")
    private Float planRepaymentInterest;

    /**
     * 剩余本金:授信金额-当期及当期之前所有期数本金和
     */
    @Column(name = "当期剩余本金")
    private Float planSurplusPrincipal;

    /**
     * 当期应还违约金
     */
    @Column(name = "违约金")
    private Float damages;

    /**
     * 当期应还总金额
     */
    @Column(name = "当期应还总金额")
    private Float planTotalAmount;

    /**
     * 计划还款类别:1212-本息 1214-服务费
     */
    @Column(name = "还款计划类别")
    private Integer planTypeId;
    /**
     * 是否坏账核销:0-否,1:是
     */
    @Column(name = "坏账核销")
    private Integer isWriteOffBadLoan;

    /**
     * 坏账核销日期
     */
    @Column(name = "坏账核销日期")
    private Date writeOffBadLoanTime;

    /**
     * 当期还款状态:2-已还,0-未还
     */
    @Column(name = "还款状态")
    private String status;


    /**
     * 当期应还罚息
     */
    @Transient
    private Float planRepaymentPenalty;

    /**
     * 是否逾期标记:0-未逾期,1-逾期
     */
    @Transient
    private  int isOverDue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDateId() {
        return dateId;
    }

    public void setDateId(Long dateId) {
        this.dateId = dateId;
    }

    public Integer getTotalNumberOfPeriods() {
        return totalNumberOfPeriods;
    }

    public void setTotalNumberOfPeriods(Integer totalNumberOfPeriods) {
        this.totalNumberOfPeriods = totalNumberOfPeriods;
    }

    public Integer getPlanTerm() {
        return planTerm;
    }

    public void setPlanTerm(Integer planTerm) {
        this.planTerm = planTerm;
    }

    public Date getPlanTermDate() {
        return planTermDate;
    }

    public void setPlanTermDate(Date planTermDate) {
        this.planTermDate = planTermDate;
    }

    public Float getPlanRepaymentPrincipal() {
        return planRepaymentPrincipal;
    }

    public void setPlanRepaymentPrincipal(Float planRepaymentPrincipal) {
        this.planRepaymentPrincipal = planRepaymentPrincipal;
    }

    public Float getPlanRepaymentInterest() {
        return planRepaymentInterest;
    }

    public void setPlanRepaymentInterest(Float planRepaymentInterest) {
        this.planRepaymentInterest = planRepaymentInterest;
    }

    public Float getPlanSurplusPrincipal() {
        return planSurplusPrincipal;
    }

    public void setPlanSurplusPrincipal(Float planSurplusPrincipal) {
        this.planSurplusPrincipal = planSurplusPrincipal;
    }

    public Float getDamages() {
        return damages;
    }

    public void setDamages(Float damages) {
        this.damages = damages;
    }

    public Float getPlanTotalAmount() {
        return planTotalAmount;
    }

    public void setPlanTotalAmount(Float planTotalAmount) {
        this.planTotalAmount = planTotalAmount;
    }

    public Integer getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(Integer planTypeId) {
        this.planTypeId = planTypeId;
    }

    public Integer getIsWriteOffBadLoan() {
        return isWriteOffBadLoan;
    }

    public void setIsWriteOffBadLoan(Integer isWriteOffBadLoan) {
        this.isWriteOffBadLoan = isWriteOffBadLoan;
    }

    public Date getWriteOffBadLoanTime() {
        return writeOffBadLoanTime;
    }

    public void setWriteOffBadLoanTime(Date writeOffBadLoanTime) {
        this.writeOffBadLoanTime = writeOffBadLoanTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Float getPlanRepaymentPenalty() {
        return planRepaymentPenalty;
    }

    public void setPlanRepaymentPenalty(Float planRepaymentPenalty) {
        this.planRepaymentPenalty = planRepaymentPenalty;
    }

    public int getIsOverDue() {
        return isOverDue;
    }

    public void setIsOverDue(int isOverDue) {
        this.isOverDue = isOverDue;
    }


}
