package org.starlightfinancial.rpc.hessian.entity.cpcn.request;

import java.io.Serializable;

/**
 * @author: Senlin.Deng
 * @Description: 由于Notice2018Request没有set方法,不能json转bean,自定义一个有相同属性的bean
 * @date: Created in 2019/4/19 11:35
 * @Modified By:
 */
public class Notice2018Req implements Serializable {
    private static final long serialVersionUID = -377705764153272002L;
    private String institutionID;
    private String txSN;
    private String traceNo;
    private long amount;
    private int status;
    private String bankTxTime;
    private String responseCode;
    private String responseMessage;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getInstitutionID() {
        return institutionID;
    }

    public void setInstitutionID(String institutionID) {
        this.institutionID = institutionID;
    }

    public String getTxSN() {
        return txSN;
    }

    public void setTxSN(String txSN) {
        this.txSN = txSN;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBankTxTime() {
        return bankTxTime;
    }

    public void setBankTxTime(String bankTxTime) {
        this.bankTxTime = bankTxTime;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
