package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.paymentbinding.Tx2512Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付快捷支付查询返回结果
 * @date: Created in 2019/5/5 16:47
 * @Modified By:
 */
public class Tx2512Res extends Tx2512Response implements Serializable, BaseResponse {


    private static final long serialVersionUID = -8730278056083853730L;

    public Tx2512Res(String responseMessage, String responseSignature) throws Exception {
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
