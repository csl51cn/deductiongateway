package org.starlightfinancial.deductiongateway.service;


import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.vo.MortgageDeductionQueryCondition;

import java.io.IOException;
import java.util.List;

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
     * 查询代扣数据
     *
     * @param mortgageDeductionQueryCondition 查询条件
     * @param createId 创建人
     * @return
     */
    PageBean queryMortgageDeductionData(MortgageDeductionQueryCondition mortgageDeductionQueryCondition, int createId);

    /**
     * 根据id查询代扣数据
     *
     * @param ids
     * @return
     */
    List<MortgageDeduction> findMortgageDeductionListByIds(String ids);

    /**
     * 导出代扣结果excel
     *
     * @param mortgageDeductionQueryCondition 查询条件
     * @return
     */
    Workbook exportXLS(MortgageDeductionQueryCondition mortgageDeductionQueryCondition);

    /**
     * 批量更新代扣数据
     *
     * @param list
     */
    void updateMortgageDeductions(List<MortgageDeduction> list);

    /**
     * 删除代扣数据
     *
     * @param list
     */
    void deleteMortgageDeductions(List<MortgageDeduction> list);

    /**
     * 根据订单号查询代扣记录
     */
    MortgageDeduction findByOrdId(String ordId);

    /**
     * 更新代扣数据
     *
     * @param mortgageDeduction
     */
    void updateMortgageDeduction(MortgageDeduction mortgageDeduction);


    /**
     * 自动上传代扣成功的记录
     * @throws IOException 深复制异常时抛出
     * @throws ClassNotFoundException 深复制异常时抛出
     */
    void uploadAutoAccountingFile() throws IOException, ClassNotFoundException;
}
