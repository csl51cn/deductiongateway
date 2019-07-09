package org.starlightfinancial.rpc.hessian.entity.yqb;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author: Senlin.Deng
 * @Description: 抽象的json返回
 * @date: Created in 2019/7/3 13:17
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class AbstractJsonTxResponse extends AbstractTxResponse {

    private static final long serialVersionUID = 850913673891133936L;
    /**
     * 请求正文 业务请求参数JSON明文串AES加密
     */
    protected String content;
    /**
     * 请求正文的摘要 正文content (明文)+散列密钥的SHA摘要，用于检验content完整性。摘要算法见散列说明
     */
    protected String token;
    /**
     * 散列算法	SHA-1
     */
    protected String hashFunc;
    /**
     * 服务码
     */
    protected String serviceCode;


    public AbstractJsonTxResponse() {
    }

    /**
     * 返回的数据有两种格式,一种是key=value&key=value...,一种是json的格式
     *
     * @param response 返回的数据
     * @throws Exception
     */
    public AbstractJsonTxResponse(String response) throws Exception {
        super(response);
    }
}
