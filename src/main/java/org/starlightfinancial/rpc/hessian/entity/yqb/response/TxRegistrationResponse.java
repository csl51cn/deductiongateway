package org.starlightfinancial.rpc.hessian.entity.yqb.response;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;
import org.starlightfinancial.rpc.hessian.entity.yqb.AbstractJsonTxResponse;

/**
 * @author: Senlin.Deng
 * @Description: 注册返回信息
 * @date: Created in 2019/7/3 14:41
 * @Modified By:
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TxRegistrationResponse extends AbstractJsonTxResponse {


    private static final long serialVersionUID = -3625842590249719018L;

    /**
     * 结果码
     */
    private String respCode;
    /**
     * 备注
     */
    private String memo;
    /**
     * 平安付会员号，需要时返回
     */
    private String customerId;
    /**
     * 平安付外部会员号，需要时返回
     */
    private String uId;

    /**
     * arc-token，需要时返回
     */
    @JsonProperty("token")
    private String arcToken;

    public TxRegistrationResponse() {
    }

    public TxRegistrationResponse(String response) throws Exception {
        super(response);
    }

    /**
     * 用已经是json格式的返回消息为属性赋值
     *
     * @throws Exception
     */
    @Override
    protected void process() throws Exception {
        TxRegistrationResponse txRegistrationResponse = JSONObject.parseObject(responseMessage, TxRegistrationResponse.class);
        BeanUtils.copyProperties(txRegistrationResponse, this);
    }
}
