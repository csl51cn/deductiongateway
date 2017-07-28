package org.starlightfinancial.deductiongateway.service;


import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 批量扣款
 *
 * @author sili.chen
 */
public interface MortgageDeductionService {

    /**
     * 导入扣款数据.
     */
    public void importCustomerData(File uploadedFile, int staffId);


    /**
     * 执行代扣.
     *
     * @param list the list
     * @return the list
     */
    public List<Map> saveMortgageDeductions(List<MortgageDeduction> list);

}
