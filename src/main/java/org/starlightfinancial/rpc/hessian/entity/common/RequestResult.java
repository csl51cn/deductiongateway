package org.starlightfinancial.rpc.hessian.entity.common;

import java.io.Serializable;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 请求结果
 * @date: Created in 2019/4/18 10:23
 * @Modified By:
 */
public class RequestResult implements Serializable {
    private static final long serialVersionUID = 6192426159417661466L;
    private String errorCode;

    private String reason;

    private List<?> result;

    public RequestResult setStatus(String errorCode, String reason) {
        this.errorCode = errorCode;
        this.reason = reason;
        return this;
    }


    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<?> getResult() {
        return result;
    }

    public void setResult(List<?> result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "RequestResult{" +
                "errorCode='" + errorCode + '\'' +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
