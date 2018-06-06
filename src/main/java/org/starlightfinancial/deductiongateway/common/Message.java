package org.starlightfinancial.deductiongateway.common;

import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;

/**
 * @author: senlin.deng
 * @Description:
 * @date: Created in 2018/5/9 16:56
 * @Modified By:
 */
public class Message {


    private String code;
    private Object data;
    private String message;


    public Message() {
    }

    public Message(String code) {
        this.code = code;
    }

    public Message(String code, Object data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static Message success() {
        return new Message(ConstantsEnum.REQUEST_SUCCESS.getCode());
    }
    public static Message success(String message) {
        return new Message(ConstantsEnum.REQUEST_SUCCESS.getCode(),null,message);
    }
    public static Message fail() {
        return fail(null);
    }

    public static Message fail(String message) {
        return new Message(ConstantsEnum.REQUEST_FAIL.getCode(), null, message);
    }
    public static Message fail(String message,Object data) {
        return new Message(ConstantsEnum.REQUEST_FAIL.getCode(), data, message);
    }
}
