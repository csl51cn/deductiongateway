package org.starlightfinancial.rpc.hessian.entity.yqb;

import org.apache.http.message.BasicNameValuePair;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 抽象的请求参数
 * @date: Created in 2019/6/27 15:38
 * @Modified By:
 */
public abstract class AbstractTxRequest implements Serializable {

    private static final long serialVersionUID = -3115890098989698526L;
    protected List<BasicNameValuePair> requestMessage;
    protected String requestJsonData;

    public List<BasicNameValuePair> getRequestMessage() {
        return requestMessage;
    }


    public String getRequestJsonData() {
        return requestJsonData;
    }

    /**
     * 获取请求类型
     *
     * @return 请求类型
     */
    public abstract String obtainSendType();

    /**
     * 处理参数
     *
     * @throws Exception
     */
    public abstract void process() throws Exception;


}
