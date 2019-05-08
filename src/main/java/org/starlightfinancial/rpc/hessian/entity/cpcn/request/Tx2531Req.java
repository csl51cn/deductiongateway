package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

import payment.api.tx.paymentbinding.Tx2531Request;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付 建立绑定关系发送验证码请求参数
 * @date: Created in 2019/5/5 15:15
 * @Modified By:
 */
public class Tx2531Req extends Tx2531Request implements Serializable, BaseRequest {
    private static final long serialVersionUID = -2839080406989305143L;
}
