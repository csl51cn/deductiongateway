package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

import payment.api.tx.realgathering.Tx2011Request;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付单笔代扣请求参数
 * @date: Created in 2019/4/16 10:05
 * @Modified By:
 */
public class Tx2011Req extends Tx2011Request implements Serializable,BaseRequest {
    private static final long serialVersionUID = -5745030818647255564L;
}
