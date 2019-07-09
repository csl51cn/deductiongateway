package org.starlightfinancial.rpc.hessian.service.yqb;

import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractTxRequest;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/6/27 16:33
 * @Modified By:
 */
public interface PingAnBaseService {


    /**
     * 与平安系统进行交易的抽象方法
     *
     * @param baseRequest 请求参数
     * @return 返回请求结果
     * @throws Exception
     */
    RequestResult doBusiness(AbstractTxRequest baseRequest) throws Exception;
}
