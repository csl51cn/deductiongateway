package org.starlightfinancial.deductiongateway.exception.nondeduction;

/**
 * @author: Senlin.Deng
 * @Description: 已上传更新异常
 * @date: Created in 2019/5/28 16:25
 * @Modified By:
 */
public class AlreadyUploadedUpdateException extends RuntimeException {
    private static final long serialVersionUID = -4826420140934679019L;

    public AlreadyUploadedUpdateException() {
    }


    public AlreadyUploadedUpdateException(String msg) {
        super(msg);
    }

    public AlreadyUploadedUpdateException(String msg, Exception nestedEx) {
        super(msg, nestedEx);
    }
}
