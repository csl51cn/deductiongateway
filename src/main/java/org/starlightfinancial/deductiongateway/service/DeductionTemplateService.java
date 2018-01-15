package org.starlightfinancial.deductiongateway.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.Date;

/**
 * 代扣模板管理Service
 */
public interface DeductionTemplateService {


    /**
     * 根据条件查询代扣模板
     * @param pageBean
     * @param isSuccess
     * @param contractNo
     * @param customerName
     * @param startDate
     * @param endDate
     * @return
     */
    PageBean queryDeductionTemplate(PageBean pageBean, String isSuccess, String contractNo, String customerName, Date startDate, Date endDate);

    /**
     * 根据条件下载代扣模板
     * @param isSuccess
     * @param contractNo
     * @param customerName
     * @param startDate
     * @param endDate
     * @return
     */
    Workbook exportXLS(String isSuccess, String contractNo, String customerName, Date startDate, Date endDate);
}
