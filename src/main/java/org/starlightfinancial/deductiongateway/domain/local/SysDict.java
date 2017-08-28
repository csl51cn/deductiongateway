package org.starlightfinancial.deductiongateway.domain.local;

import javax.persistence.*;

/**
 * The persistent class for the SYS_DICT database table.
 */
@Entity(name = "SYS_DICT")
public class SysDict {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "dic_desc")
    private String dicDesc;

    @Column(name = "dic_key")
    private String dicKey;

    @Column(name = "dic_order")
    private int dicOrder;

    @Column(name = "dic_type")
    private String dicType;

    @Column(name = "dic_value")
    private String dicValue;

    @Column(name = "isedit")
    private int isedit;

    @Column(name = "parentid")
    private int parentid;

    public SysDict() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDicDesc() {
        return dicDesc;
    }

    public void setDicDesc(String dicDesc) {
        this.dicDesc = dicDesc;
    }

    public String getDicKey() {
        return dicKey;
    }

    public void setDicKey(String dicKey) {
        this.dicKey = dicKey;
    }

    public int getDicOrder() {
        return dicOrder;
    }

    public void setDicOrder(int dicOrder) {
        this.dicOrder = dicOrder;
    }

    public String getDicType() {
        return dicType;
    }

    public void setDicType(String dicType) {
        this.dicType = dicType;
    }

    public String getDicValue() {
        return dicValue;
    }

    public void setDicValue(String dicValue) {
        this.dicValue = dicValue;
    }

    public int getIsedit() {
        return isedit;
    }

    public void setIsedit(int isedit) {
        this.isedit = isedit;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }
}