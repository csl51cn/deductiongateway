package org.starlightfinancial.deductiongateway.service;

import java.io.IOException;

/**
 * @author: Senlin.Deng
 * @Description: 财务凭证管理service
 * @date: Created in 2018/8/10 16:26
 * @Modified By:
 */
public interface FinancialVoucherService {

    /**
     * 导入还款数据
     * @throws IOException 深复制异常时抛出
     * @throws ClassNotFoundException 深复制异常时抛出
     */
    void importRepaymentData() throws IOException, ClassNotFoundException;
}
