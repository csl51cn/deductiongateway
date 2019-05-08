package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.paymentbinding.Tx2511Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付快捷支付返回结果
 * @date: Created in 2019/5/5 16:47
 * @Modified By:
 */
public class Tx2511Res extends Tx2511Response implements Serializable, BaseResponse {


    private static final long serialVersionUID = 5770381587868358092L;

    public Tx2511Res(String responseMessage, String responseSignature) throws Exception {
        super(responseMessage, responseSignature);
    }

    /**
     * 获取完整的响应对象
     *
     * @return
     */
    @Override
    public Object getResponse() {
        return this;
    }
}
