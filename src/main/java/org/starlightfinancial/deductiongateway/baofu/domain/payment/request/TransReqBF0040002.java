package org.starlightfinancial.deductiongateway.baofu.domain.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Senlin.Deng
 * @Description: 代付交易查证
 * @date: Created in 2018/9/12 9:18
 * @Modified By:
 */
public class TransReqBF0040002 {


    /**
     * 宝付批次号
     */
    @JsonProperty(value = "trans_batchid")
    private String transBatchId;

    /**
     * 商户订单号
     */
    @JsonProperty(value = "trans_no")
    private String transNo;

    public String getTransBatchId() {
        return transBatchId;
    }

    public void setTransBatchId(String transBatchId) {
        this.transBatchId = transBatchId;
    }

    public String getTransNo() {
        return transNo;
    }

    public void setTransNo(String transNo) {
        this.transNo = transNo;
    }

    @Override
    public String toString() {
        return "TransReqBF0040002{" +
                "transBatchId='" + transBatchId + '\'' +
                ", transNo='" + transNo + '\'' +
                '}';
    }
}
