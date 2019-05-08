package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 关联还款人
 * @date: Created in 2018/7/23 11:35
 * @Modified By:
 */
@Entity(name = "BU_ASSOCIATED_PAYER")
public class AssociatePayer {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /**
     * 业务编号
     */
    @Column(name = "date_id")
    private Long dateId;

    /**
     * 合同编号
     */
    @Column(name = "contract_no")
    private String contractNo;


    /**
     * 关联还款人1
     */
    @Column(name = "payer1")
    private String payer1;


    /**
     * 关联还款人2
     */
    @Column(name = "payer2")
    private String payer2;
    /**
     * 关联还款人3
     */
    @Column(name = "payer3")
    private String payer3;
    /**
     * 关联还款人4
     */
    @Column(name = "payer4")
    private String payer4;
    /**
     * 创建时间
     */
    @Column(name = "gmt_create")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @Column(name = "gmt_modified")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date gmtModified;


    /**
     * 创建人id
     */
    @Column(name = "create_id")
    private Integer createId;

    /**
     * 最后一个修改人id
     */
    @Column(name = "modified_id")
    private Integer modifiedId;


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

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPayer1() {
        return payer1;
    }

    public void setPayer1(String payer1) {
        this.payer1 = payer1;
    }

    public String getPayer2() {
        return payer2;
    }

    public void setPayer2(String payer2) {
        this.payer2 = payer2;
    }

    public String getPayer3() {
        return payer3;
    }

    public void setPayer3(String payer3) {
        this.payer3 = payer3;
    }

    public String getPayer4() {
        return payer4;
    }

    public void setPayer4(String payer4) {
        this.payer4 = payer4;
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

    public Integer getCreateId() {
        return createId;
    }

    public void setCreateId(Integer createId) {
        this.createId = createId;
    }

    public Integer getModifiedId() {
        return modifiedId;
    }

    public void setModifiedId(Integer modifiedId) {
        this.modifiedId = modifiedId;
    }
}
