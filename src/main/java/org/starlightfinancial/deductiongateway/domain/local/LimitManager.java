package org.starlightfinancial.deductiongateway.domain.local;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 限额管理实体类
 *
 * @author senlin.deng
 */
@Entity(name = "BU_LIMITMANAGER")
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
}
