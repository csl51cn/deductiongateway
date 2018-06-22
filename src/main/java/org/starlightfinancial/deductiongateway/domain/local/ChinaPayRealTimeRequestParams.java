package org.starlightfinancial.deductiongateway.domain.local;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 银联快捷支付交易请求参数
 * @date: Created in 2018/5/15 16:37
 * @Modified By:
 */
public class ChinaPayRealTimeRequestParams {

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


    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 证件类型
     */
    private String certType;

    /**
     * 证件号
     */
    private String certNo;

    /**
     * 账户名
     */
    private String accName;

    /**
     * 手机号码
     */
    private String mobile;

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

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 转换为BasicNameValuePair集合
     *
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
        basicNameValuePairs.add(new BasicNameValuePair("CardNo", this.cardNo));
        basicNameValuePairs.add(new BasicNameValuePair("CertType", this.certType));
        basicNameValuePairs.add(new BasicNameValuePair("CertNo", this.certNo));
        basicNameValuePairs.add(new BasicNameValuePair("AccName", this.accName));
        basicNameValuePairs.add(new BasicNameValuePair("MobileNo", this.mobile));
        return basicNameValuePairs;
    }


}
