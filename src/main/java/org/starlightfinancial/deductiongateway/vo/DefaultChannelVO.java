package org.starlightfinancial.deductiongateway.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description: 默认渠道VO
 * @date: Created in 2020/6/29 13:28
 * @Modified By:
 */
@Data
public class DefaultChannelVO {

    private Integer id;


    /**
     * 银行代码
     */
    private String bankCode;

    /**
     * 银行名称
     */
    private String bankName;


    /**
     * 默认渠道,保存{@link org.starlightfinancial.deductiongateway.enums.DeductionChannelEnum} 中的渠道code
     */
    private String defaultChannel;


    /**
     * 创建人
     */

    private Integer createBy;

    /**
     * 修改人
     */
    private Integer lastModifiedBy;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS", timezone = "GMT+8")
    private Date gmtModified;

}
