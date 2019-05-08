package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

import payment.api.tx.paymentbinding.Tx2512Request;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付  快捷支付查询结果请求参数
 * @date: Created in 2019/5/5 15:15
 * @Modified By:
 */
public class Tx2512Req extends Tx2512Request implements Serializable, BaseRequest {
    private static final long serialVersionUID = 1201716030459173934L;
}
