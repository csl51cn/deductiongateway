package org.starlightfinancial.rpc.hessian.service.cpcn;

import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.BaseRequest;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/4/16 10:20
 * @Modified By:
 */
public interface BaseService {


    /**
     * 与中金系统进行交易的抽象方法
     *
     * @param  baseRequest 请求参数
     * @return 返回请求结果
     * @throws Exception
     */
    RequestResult doBusiness(BaseRequest baseRequest) throws Exception;
}
