package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.paymentbinding.Tx2531Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金支付 建立绑定关系发送验证码返回结果
 * @date: Created in 2019/5/5 16:47
 * @Modified By:
 */
public class Tx2531Res extends Tx2531Response implements Serializable, BaseResponse {


    private static final long serialVersionUID = 2077606994622743597L;

    public Tx2531Res(String responseMessage, String responseSignature) throws Exception {
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
