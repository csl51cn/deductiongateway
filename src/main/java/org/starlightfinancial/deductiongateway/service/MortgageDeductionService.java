package org.starlightfinancial.deductiongateway.service;


import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.Date;
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
     * @param startDate
     * @param endDate
     * @param customerName
     * @param pageBean
     * @param type         0:已执行代扣,1:未代扣数据
     * @param contractNo   合同编号
     * @param creatid
     * @return
     */
    PageBean queryMortgageDeductionData(Date startDate, Date endDate, String customerName, PageBean pageBean, String type, String contractNo, int creatid);

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
     * @param startDate
     * @param endDate
     * @param customerName
     * @return
     */
    Workbook exportXLS(Date startDate, Date endDate, String customerName);

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
     */
    void uploadAutoAccountingFile();
}
