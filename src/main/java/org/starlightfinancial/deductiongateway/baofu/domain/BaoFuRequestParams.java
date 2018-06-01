package org.starlightfinancial.deductiongateway.baofu.domain;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 宝付分账支付请求参数
 * @date: Created in 2018/5/29 9:30
 * @Modified By:
 */
public class BaoFuRequestParams {


    /**
     * 订单号
     */
    private String transId;

    /**
     * 协议号
     */
    private String protocolNo;

    /**
     * 订单金额,单位为分
     */
    private String txnAmt;

    /**
     * 分账信息
     */
    private String shareInfo;

    /**
     * 报文发送时间,格式yyyy-MM-dd HH:mm:ss
     */
    private String  sendTime;


    public String getProtocolNo() {
        return protocolNo;
    }

    public void setProtocolNo(String protocolNo) {
        this.protocolNo = protocolNo;
    }

    public String getTxnAmt() {
        return txnAmt;
    }

    public void setTxnAmt(String txnAmt) {
        this.txnAmt = txnAmt;
    }

    public String getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(String shareInfo) {
        this.shareInfo = shareInfo;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    /**
     * 转换为BasicNameValuePair集合
     * @return
     */
    public List<BasicNameValuePair> transToNvpList() {
        List<BasicNameValuePair> basicNameValuePairs = new ArrayList<>();
        basicNameValuePairs.add(new BasicNameValuePair("transId", this.transId));
        basicNameValuePairs.add(new BasicNameValuePair("protocolNo", this.protocolNo));
        basicNameValuePairs.add(new BasicNameValuePair("txnAmt", this.txnAmt));
        basicNameValuePairs.add(new BasicNameValuePair("shareInfo", this.shareInfo));
        basicNameValuePairs.add(new BasicNameValuePair("sendTime", this.sendTime));
        return basicNameValuePairs;
    }
}
