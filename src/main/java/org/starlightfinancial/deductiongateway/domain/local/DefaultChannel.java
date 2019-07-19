package org.starlightfinancial.deductiongateway.domain.local;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 各银行默认渠道
 * @date: Created in 2019/7/10 10:00
 * @Modified By:
 */
@Data
@Entity(name = "BU_DEFAULT_CHANNEL")
@EntityListeners(AuditingEntityListener.class)
public class DefaultChannel {

    @Id
    @GeneratedValue()
    private Integer id;


    /**
     * 银行代码
     */
    @Column(name = "bank_code")
    private String bankCode;

    /**
     * 银行名称
     */
    @Column(name = "bank_name")
    private String bankName;


    /**
     * 默认渠道,保存{@link org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum} 中的渠道code
     */
    @Column(name = "default_channel")
    private String defaultChannel;


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


}
