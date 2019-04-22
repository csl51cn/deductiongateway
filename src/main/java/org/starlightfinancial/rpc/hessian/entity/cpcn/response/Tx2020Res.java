package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

import payment.api.tx.realgathering.Tx2020Response;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 中金单笔代扣查询返回结果
 * @date: Created in 2019/4/18 13:38
 * @Modified By:
 */
public class Tx2020Res extends Tx2020Response implements Serializable, BaseResponse {

    private static final long serialVersionUID = 2555759256959423894L;

    public Tx2020Res(String responseMessage, String responseSignature) throws Exception {
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
