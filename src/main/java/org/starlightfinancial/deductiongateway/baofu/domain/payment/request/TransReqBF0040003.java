package org.starlightfinancial.deductiongateway.baofu.domain.payment.request;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author: Senlin.Deng
 * @Description: 代付交易退款查证
 * @date: Created in 2018/9/12 9:38
 * @Modified By:
 */
public class TransReqBF0040003 {

    /**
     *  查询起始时间,格式：YYYYMMDD（最多查询一天记录）
     */
    @JsonProperty(value = "trans_btime")
    private String transBeginTime;

    /**
     * 查询结束时间,格式：YYYYMMDD（最多查询一天记录）
     */
    @JsonProperty(value = "trans_etime")
    private String transEndTime;

    public String getTransBeginTime() {
        return transBeginTime;
    }

    public void setTransBeginTime(String transBeginTime) {
        this.transBeginTime = transBeginTime;
    }

    public String getTransEndTime() {
        return transEndTime;
    }

    public void setTransEndTime(String transEndTime) {
        this.transEndTime = transEndTime;
    }
}
