package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/4/18 14:29
 * @Modified By:
 */
public interface BaseRequest {

    /**
     * 处理参数
     * @throws Exception
     */
    void process() throws Exception;

    /**
     * 获取请求参数
     * @return
     */
    String getRequestMessage();


    /**
     * 获取请求签名
     * @return
     */
    String getRequestSignature();


}
