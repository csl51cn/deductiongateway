package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.realgathering.Tx2011Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/4/18 11:25
 * @Modified By:
 */
public class Tx2011Res extends Tx2011Response implements Serializable,BaseResponse {
    private static final long serialVersionUID = -253144755508096207L;

    public Tx2011Res(String responseMessage, String responseSignature) throws Exception {
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


