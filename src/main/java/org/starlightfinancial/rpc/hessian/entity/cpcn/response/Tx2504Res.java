package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.paymentbinding.Tx2504Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/5/5 16:47
 * @Modified By:
 */
public class Tx2504Res extends Tx2504Response implements Serializable, BaseResponse {


    private static final long serialVersionUID = 1460656626989267935L;

    public Tx2504Res(String responseMessage, String responseSignature) throws Exception {
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
