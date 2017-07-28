package org.starlightfinancial.deductiongateway.service;


import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;

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
    public void importCustomerData(MultipartFile uploadedFile, int staffId);


    /**
     * 执行代扣.
     *
     * @param list the list
     * @return the list
     */
    public List<Map> saveMortgageDeductions(List<MortgageDeduction> list);

}
