package org.starlightfinancial.deductiongateway.baofu.domain.payment;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 循环节点包裹类,仅为满足json的结构
 * @date: Created in 2018/9/11 16:43
 * @Modified By:
 */

public class TransReqDataWrapper<T> {

    @JsonProperty(value = "trans_reqData")
    private List<T> transReqData;

    public List<T> getTransReqData() {
        return transReqData;
    }

    public void setTransReqData(List<T> transReqData) {
        this.transReqData = transReqData;
    }

    @Override
    public String toString() {
        return "TransReqDataWrapper{" +
                "transReqData=" + transReqData +
                '}';
    }
}
