package org.starlightfinancial.deductiongateway.domain.local;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 银联分账支付交易请求参数
 * @date: Created in 2018/5/15 16:37
 * @Modified By:
 */
public class UnionPayRequestParams {

    /**
     * 订单号
     */
    private String merOrderNo;


    /**
     * 协议号
     */
    private String protocolNo;


    /**
     * 分账类型
     * 0001:实时分账(下一个结算日分账)
     * 0002:延时分账(需要商户再发起通知分账)
     */
    private String splitType;

    /**
     * 分账方式
     * 0:按金额分账
     * 1:按比例分账
     */
    private String splitMethod;

    /**
     * 分账信息:分账商户号A^金额或比例;分账商户B^金额或比例
     */
    private String merSplitMsg;


    /**
     * 订单金额,单位为分
     */
    private String orderAmt;


    public String getMerOrderNo() {
        return merOrderNo;
    }

    public void setMerOrderNo(String merOrderNo) {
        this.merOrderNo = merOrderNo;
    }

    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getSplitType() {
        return splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }

    public String getSplitMethod() {
        return splitMethod;
    }

    public void setSplitMethod(String splitMethod) {
        this.splitMethod = splitMethod;
    }

    public String getMerSplitMsg() {
        return merSplitMsg;
    }

    public void setMerSplitMsg(String merSplitMsg) {
        this.merSplitMsg = merSplitMsg;
    }

    public String getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(String orderAmt) {
        this.orderAmt = orderAmt;
    }

    /**
     * 转换为BasicNameValuePair集合
     * @return
     */
    public List<BasicNameValuePair> transToNvpList() {
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("MerOrderNo", this.merOrderNo));
        basicNameValuePairs.add(new BasicNameValuePair("ProtocolNo", this.protocolNo));
        basicNameValuePairs.add(new BasicNameValuePair("SplitType", this.splitType));
        basicNameValuePairs.add(new BasicNameValuePair("SplitMethod", this.splitMethod));
        basicNameValuePairs.add(new BasicNameValuePair("MerSplitMsg", this.merSplitMsg));
        basicNameValuePairs.add(new BasicNameValuePair("OrderAmt", this.orderAmt));
        return basicNameValuePairs;
    }


}
