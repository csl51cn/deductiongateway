package org.starlightfinancial.deductiongateway.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.Date;

/**
 * 代扣模板管理Service
 */
public interface DeductionTemplateService {


    PageBean queryDeductionTemplate(PageBean pageBean, String isSuccess, String contractNo, String bizNo, Date startDate, Date endDate);

    Workbook exportXLS(String isSuccess, String contractNo, String bizNo, Date startDate, Date endDate);
}
