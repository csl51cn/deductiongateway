package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 限额管理实体类
 *
 * @author senlin.deng
 */
@Entity(name = "BU_LIMITMANAGER")
@EntityListeners(AuditingEntityListener.class)
public class LimitManager {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 银行代码
     */
    @Column(name = "bankcode")
    private String bankCode;

    /**
     * 银行名称
     */
    @Column(name = "bankname")
    private String bankName;


    /**
     * 卡性质,0表示借记卡,1表示贷记卡,2表示准贷记卡
     */
    @Column(name = "cardtype")
    private Integer cardType;

    /**
     * 单笔单卡限额 :-1时为无限额
     */
    @Column(name = "singlelimit")
    private BigDecimal singleLimit;

    /**
     * 单日单笔限额 -1时为无限额
     */
    @Column(name = "singledaylimit")
    private BigDecimal singleDayLimit;

    /**
     * 支付渠道
     */
    @Column(name = "channel")
    private String channel;

    /**
     * 是否启用支付渠道:0-否,1-是
     */
    @Column(name = "is_enabled")
    private String enabled;


    /**
     * 是否支持悦至渝分账:0-否,1-是
     */
//    @Column(name = "support_yuezhiyu")
    @Transient
    private String supportYuezhiyu;

    /**
     * 创建人
     */
    @Column(name = "create_by")
    @CreatedBy
    private Integer createBy;

    /**
     * 修改人
     */
    @Column(name = "last_modified_by")
    @LastModifiedBy
    private Integer lastModifiedBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @Column(name = "gmt_create")
    @CreatedDate
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    @Column(name = "gmt_modified")
    @LastModifiedDate
    private Date gmtModified;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(BigDecimal singleLimit) {
        this.singleLimit = singleLimit;
    }

    public BigDecimal getSingleDayLimit() {
        return singleDayLimit;
    }

    public void setSingleDayLimit(BigDecimal singleDayLimit) {
        this.singleDayLimit = singleDayLimit;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Integer getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Integer lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getSupportYuezhiyu() {
        return supportYuezhiyu;
    }

    public void setSupportYuezhiyu(String supportYuezhiyu) {
        this.supportYuezhiyu = supportYuezhiyu;
    }
}
