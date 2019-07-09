package org.starlightfinancial.rpc.hessian.entity.yqb.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 代扣分账信息
 * @date: Created in 2019/7/5 9:26
 * @Modified By:
 */
@Data
@Builder
public class Tx001SplitInfo implements Serializable {

    private static final long serialVersionUID = 217054473088239896L;
    /**
     * 分账商户号
     */
    private String merchantId;
    /**
     * 商户类型(B:商户，C:个人) 
     */
    private String merchantType;
    /**
     * 分账流水号
     */
    private String splitOrderNo;
    /**
     * 分账金额(单位：分) 分账金额相加等于订单金额 分账金额包含分佣金额
     */
    private String splitAmt;
    /**
     * 分佣金额(单位：分)
     */
    private String commAmt;
}
