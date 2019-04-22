package org.starlightfinancial.rpc.hessian.entity.cpcn.response;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/4/18 14:20
 * @Modified By:
 */
public interface BaseResponse {

    /**
     * 响应码
     * @return
     */
    String getCode();

    /**
     * 响应消息
     * @return
     */
    String getMessage();

    /**
     * Base64解码后的消息
     * @return
     */
    String getResponsePlainText();

    /**
     * 获取完整的响应对象
     * @return
     */
    Object getResponse();
}
