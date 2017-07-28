package org.starlightfinancial.deductiongateway.domain;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the SYS_DICT database table.
 */
@Entity
public class SysAutoNum implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "type")
    private String type;

    @Column(name = "is_date")
    private String isDate;


    @Column(name = "corp_pre")
    private String corpPre;

    @Column(name = "type_pre")
    private String typePre;

    @Column(name = "init_no")
    private String initNo;

    @Column(name = "now_no")
    private String nowNo;

    @Column(name = "step_no")
    private int stepNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsDate() {
        return isDate;
    }

    public void setIsDate(String isDate) {
        this.isDate = isDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCorpPre() {
        return corpPre;
    }

    public void setCorpPre(String corpPre) {
        this.corpPre = corpPre;
    }

    public String getTypePre() {
        return typePre;
    }

    public void setTypePre(String typePre) {
        this.typePre = typePre;
    }

    public String getInitNo() {
        return initNo;
    }

    public void setInitNo(String initNo) {
        this.initNo = initNo;
    }

    public String getNowNo() {
        return nowNo;
    }

    public void setNowNo(String nowNo) {
        this.nowNo = nowNo;
    }

    public int getStepNo() {
        return stepNo;
    }

    public void setStepNo(int stepNo) {
        this.stepNo = stepNo;
    }

}