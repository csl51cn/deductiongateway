package org.starlightfinancial.deductiongateway.domain.remote;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 还款登记表
 * @date: Created in 2018/8/28 16:47
 * @Modified By:
 */
@Entity(name = "Date_还款登记表")
public class RepaymentActual {

    /**
     * 主键
     */
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 业务dateId
     */
    @Column(name = "Date_Id")
    private Long dateId;

    /**
     * 还款期数
     */
    @Column(name = "还款期数")
    private Integer repaymentTerm;

    /**
     * 还款日期
     */
    @Column(name = "还款日期")
    private Date repaymentTermDate;


    /**
     * 当期应还本金
     */
    @Column(name = "应还本金")
    private Float planRepaymentPrincipal;


    /**
     * 当期实还本金
     */
    @Column(name = "实还本金")
    private Float actualRepaymentPrincipal;

    /**
     * 当期未还本金
     */
    @Column(name = "未还本金")
    private Float surplusRepaymentPrincipal;


    /**
     * 当期应还利息
     */
    @Column(name = "应还利息")
    private Float planRepaymentInterest;


    /**
     * 当期实还利息
     */
    @Column(name = "实还利息")
    private Float actualRepaymentInterest;

    /**
     * 当期未还利息
     */
    @Column(name = "未还利息")
    private Float surplusRepaymentInterest;


    /**
     * 当期应还罚息
     */
    @Column(name = "应还罚息")
    private Float planRepaymentPenalty;


    /**
     * 当期实还罚息
     */
    @Column(name = "实还罚息")
    private Float actualRepaymentPenalty;

    /**
     * 当期未还罚息
     */
    @Column(name = "未还罚息")
    private Float surplusRepaymentPenalty;


    /**
     * 当期应还违约金
     */
    @Column(name = "应还费用Two")
    private Float planRepaymentDamage;


    /**
     * 当期实还违约金
     */
    @Column(name = "实还费用Two")
    private Float actualRepaymentDamage;

    /**
     * 当期未还违约金
     */
    @Column(name = "未还费用Two")
    private Float surplusRepaymentDamage;

    /**
     * 当期实还金额
     */
    @Column(name = "实还金额")
    private Float actualRepaymentAmount;


    /**
     * 当期还款总额=当期实还金额 +  当期已用挂账
     */
    @Column(name = "还款总额")
    private Float totalRepaymentAmount;

    /**
     * 剩余挂账
     */
    @Column(name = "剩余挂账")
    private Float surplusHangingAmount;

    /**
     * 已用挂账
     */
    @Column(name = "已用挂账")
    private Float usedHangingAmount;

    /**
     * 挂号合计
     */
    @Column(name = "挂账合计")
    private Float totalHangingAmount;

    /**
     * 是否逾期标记:0-未逾期,1-逾期
     */
    @Column(name = "是否逾期")
    private Integer isOverdue;

    /**
     * 逾期天数
     */
    @Column(name = "逾期天数")
    private Integer overdueDays;

    /**
     * 当期应还罚息
     */
    @Column(name = "逾期利息")
    private Float overduePenalty;

    /**
     * 是否记入征信标记:0-不计入,1-记入
     */
    @Column(name = "是否记入征信")
    private Integer isAddCreditRecord;


    /**
     * 还款方式
     */
    @Column(name = "还款方式")
    private String repaymentMethod;

    /**
     * 收费公司id
     */
    @Column(name = "银行助记码")
    private String chargeCompany;

    /**
     * 备注
     */
    @Column(name = "备注说明")
    private String remark;


    /**
     * 登记人
     */
    @Column(name = "登记人")
    private int registrantId;


    /**
     * 登记时间
     */
    @Column(name = "登记时间")
    private Date recordedDate;

    /**
     * 还款类别
     */
    @Column(name = "还款计划类别")
    private Integer planTypeId;


    /**
     * 入账时间
     */
    @Column(name = "入账日期")
    private Date accountingDate;

    /**
     * 是否代扣
     */
    @Column(name = "是否代扣")
    private String isDeduction;


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

    public Integer getRepaymentTerm() {
        return repaymentTerm;
    }

    public void setRepaymentTerm(Integer repaymentTerm) {
        this.repaymentTerm = repaymentTerm;
    }

    public Date getRepaymentTermDate() {
        return repaymentTermDate;
    }

    public void setRepaymentTermDate(Date repaymentTermDate) {
        this.repaymentTermDate = repaymentTermDate;
    }

    public Float getPlanRepaymentPrincipal() {
        return planRepaymentPrincipal;
    }

    public void setPlanRepaymentPrincipal(Float planRepaymentPrincipal) {
        this.planRepaymentPrincipal = planRepaymentPrincipal;
    }

    public Float getActualRepaymentPrincipal() {
        return actualRepaymentPrincipal;
    }

    public void setActualRepaymentPrincipal(Float actualRepaymentPrincipal) {
        this.actualRepaymentPrincipal = actualRepaymentPrincipal;
    }

    public Float getSurplusRepaymentPrincipal() {
        return surplusRepaymentPrincipal;
    }

    public void setSurplusRepaymentPrincipal(Float surplusRepaymentPrincipal) {
        this.surplusRepaymentPrincipal = surplusRepaymentPrincipal;
    }

    public Float getPlanRepaymentInterest() {
        return planRepaymentInterest;
    }

    public void setPlanRepaymentInterest(Float planRepaymentInterest) {
        this.planRepaymentInterest = planRepaymentInterest;
    }

    public Float getActualRepaymentInterest() {
        return actualRepaymentInterest;
    }

    public void setActualRepaymentInterest(Float actualRepaymentInterest) {
        this.actualRepaymentInterest = actualRepaymentInterest;
    }

    public Float getSurplusRepaymentInterest() {
        return surplusRepaymentInterest;
    }

    public void setSurplusRepaymentInterest(Float surplusRepaymentInterest) {
        this.surplusRepaymentInterest = surplusRepaymentInterest;
    }

    public Float getPlanRepaymentPenalty() {
        return planRepaymentPenalty;
    }

    public void setPlanRepaymentPenalty(Float planRepaymentPenalty) {
        this.planRepaymentPenalty = planRepaymentPenalty;
    }

    public Float getActualRepaymentPenalty() {
        return actualRepaymentPenalty;
    }

    public void setActualRepaymentPenalty(Float actualRepaymentPenalty) {
        this.actualRepaymentPenalty = actualRepaymentPenalty;
    }

    public Float getSurplusRepaymentPenalty() {
        return surplusRepaymentPenalty;
    }

    public void setSurplusRepaymentPenalty(Float surplusRepaymentPenalty) {
        this.surplusRepaymentPenalty = surplusRepaymentPenalty;
    }

    public Float getPlanRepaymentDamage() {
        return planRepaymentDamage;
    }

    public void setPlanRepaymentDamage(Float planRepaymentDamage) {
        this.planRepaymentDamage = planRepaymentDamage;
    }

    public Float getActualRepaymentDamage() {
        return actualRepaymentDamage;
    }

    public void setActualRepaymentDamage(Float actualRepaymentDamage) {
        this.actualRepaymentDamage = actualRepaymentDamage;
    }

    public Float getSurplusRepaymentDamage() {
        return surplusRepaymentDamage;
    }

    public void setSurplusRepaymentDamage(Float surplusRepaymentDamage) {
        this.surplusRepaymentDamage = surplusRepaymentDamage;
    }

    public Float getActualRepaymentAmount() {
        return actualRepaymentAmount;
    }

    public void setActualRepaymentAmount(Float actualRepaymentAmount) {
        this.actualRepaymentAmount = actualRepaymentAmount;
    }

    public Float getTotalRepaymentAmount() {
        return totalRepaymentAmount;
    }

    public void setTotalRepaymentAmount(Float totalRepaymentAmount) {
        this.totalRepaymentAmount = totalRepaymentAmount;
    }

    public Float getSurplusHangingAmount() {
        return surplusHangingAmount;
    }

    public void setSurplusHangingAmount(Float surplusHangingAmount) {
        this.surplusHangingAmount = surplusHangingAmount;
    }

    public Float getUsedHangingAmount() {
        return usedHangingAmount;
    }

    public void setUsedHangingAmount(Float usedHangingAmount) {
        this.usedHangingAmount = usedHangingAmount;
    }

    public Float getTotalHangingAmount() {
        return totalHangingAmount;
    }

    public void setTotalHangingAmount(Float totalHangingAmount) {
        this.totalHangingAmount = totalHangingAmount;
    }

    public Integer getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Integer isOverdue) {
        this.isOverdue = isOverdue;
    }

    public Integer getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Integer overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Float getOverduePenalty() {
        return overduePenalty;
    }

    public void setOverduePenalty(Float overduePenalty) {
        this.overduePenalty = overduePenalty;
    }

    public Integer getIsAddCreditRecord() {
        return isAddCreditRecord;
    }

    public void setIsAddCreditRecord(Integer isAddCreditRecord) {
        this.isAddCreditRecord = isAddCreditRecord;
    }

    public String getRepaymentMethod() {
        return repaymentMethod;
    }

    public void setRepaymentMethod(String repaymentMethod) {
        this.repaymentMethod = repaymentMethod;
    }

    public String getChargeCompany() {
        return chargeCompany;
    }

    public void setChargeCompany(String chargeCompany) {
        this.chargeCompany = chargeCompany;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getRegistrantId() {
        return registrantId;
    }

    public void setRegistrantId(int registrantId) {
        this.registrantId = registrantId;
    }

    public Date getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(Date recordedDate) {
        this.recordedDate = recordedDate;
    }

    public Integer getPlanTypeId() {
        return planTypeId;
    }

    public void setPlanTypeId(Integer planTypeId) {
        this.planTypeId = planTypeId;
    }

    public Date getAccountingDate() {
        return accountingDate;
    }

    public void setAccountingDate(Date accountingDate) {
        this.accountingDate = accountingDate;
    }

    public String getIsDeduction() {
        return isDeduction;
    }

    public void setIsDeduction(String isDeduction) {
        this.isDeduction = isDeduction;
    }
}
