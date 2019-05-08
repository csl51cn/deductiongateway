package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

import payment.api.tx.paymentbinding.Tx2511Request;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付 进行快捷支付请求参数
 * @date: Created in 2019/5/5 15:15
 * @Modified By:
 */
public class Tx2511Req extends Tx2511Request implements Serializable, BaseRequest {
    private static final long serialVersionUID = -2066233778094875822L;
}
