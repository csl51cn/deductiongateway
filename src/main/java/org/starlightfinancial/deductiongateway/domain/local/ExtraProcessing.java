package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 额外处理实体类
 */
@Entity(name = "BU_EXTRA_PROCESSING")
public class ExtraProcessing {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * 产品类别
     */
    @Column(name = "product")
    private String product;


    /**
     * 节假日自动代扣状态:0表示关闭,1表示开启
     */
    @Column(name = "status")
    private String status;

    /**
     * 修改时间
     */
    @Column(name = "modifydate")
    @JsonFormat(pattern = "yyyy/MM/dd.HH:mm:ss", timezone = "GMT+8")
    private Date modifyDate;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
