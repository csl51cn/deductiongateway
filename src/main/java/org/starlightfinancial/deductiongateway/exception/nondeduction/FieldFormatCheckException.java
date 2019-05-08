package org.starlightfinancial.deductiongateway.exception.nondeduction;

/**
 * @author: Senlin.Deng
 * @Description: 属性格式校验异常
 * @date: Created in 2018/8/1 16:06
 * @Modified By:
 */
public class FieldFormatCheckException extends RuntimeException {


    public FieldFormatCheckException(String msg) {
        super(msg);
    }

    public FieldFormatCheckException(String msg,Exception nestedEx) {
        super(msg, nestedEx);
    }



}
