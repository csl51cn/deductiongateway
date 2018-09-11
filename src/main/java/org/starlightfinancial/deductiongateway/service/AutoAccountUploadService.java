package org.starlightfinancial.deductiongateway.service;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.starlightfinancial.deductiongateway.domain.local.AutoAccountingExcelRow;
import org.starlightfinancial.deductiongateway.utility.AutoAccountingFtpUtil;
import org.starlightfinancial.deductiongateway.utility.HttpClientUtil;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 自动入账上传服务类
 * @date: Created in 2018/7/24 10:12
 * @Modified By:
 */
public class AutoAccountUploadService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutoAccountUploadService.class);
    private static String[] autoAccountingHeaders = new String[]{
            "客户编号", "客户名称", "合同编号", "还款日期", "入账日期", "开户行", "卡号/折号", "账数据１金额（元）",
            "分账数据２金额（元）", "分账数据3金额（元）", "分账数据4金额（元）", "收分账数据２的公司", "持卡人姓名",
            "证件号", "扣款结果", "原因", "银联分账数据１扣款(分)", "银联分账数据2扣款(分)", "是否已核销", "是否代扣", "收分账数据3的公司", "还款方式"
    };

    private static  Environment environment = SpringContextUtil.getBean(Environment.class);

    /**
     * 生成自动入账excel文档
     *
     * @param autoAccountingExcelRows 代扣记录
     * @return 自动入账excel文档
     */
    public static HSSFWorkbook createWorkBook(List<AutoAccountingExcelRow> autoAccountingExcelRows) {

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("sheet1");
        //表头
        HSSFRow headRow = sheet.createRow(0);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFFont font = workbook.createFont();
        font.setBold(true);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setFont(font);
        for (int i = 0; i < autoAccountingHeaders.length; i++) {
            HSSFCell cell = headRow.createCell(i);
            cell.setCellValue(autoAccountingHeaders[i]);
            cell.setCellStyle(cellStyle);
        }
        HSSFRow row = null;
        HSSFCell cell = null;
        HSSFCellStyle dateStyle = workbook.createCellStyle();
        dateStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("yyyy/mm/dd"));
        HSSFCellStyle numericStyle = workbook.createCellStyle();
        numericStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
        int i = 1;
        for (AutoAccountingExcelRow autoAccountingExcelRow : autoAccountingExcelRows) {
            row = sheet.createRow(i);
            //客户编号
            cell = row.createCell(0);
            cell.setCellValue(i);
            //客户名称
            cell = row.createCell(1);
            cell.setCellValue(autoAccountingExcelRow.getCustomerName());
            //合同编号
            cell = row.createCell(2);
            cell.setCellValue(autoAccountingExcelRow.getContractNo());
            //还款时间
            cell = row.createCell(3);
            cell.setCellValue(autoAccountingExcelRow.getRepaymentTermDate());
            //入账时间
            cell = row.createCell(4);
            cell.setCellValue(autoAccountingExcelRow.getAccountingDate());
            //开户行
            cell = row.createCell(5);
            cell.setCellValue("");
            //卡号/折号
            cell = row.createCell(6);
            cell.setCellValue("");
            //分账数据１金额（元）
            cell = row.createCell(7);
            cell.setCellValue(autoAccountingExcelRow.getPrincipalAndInterest().toString());
            //分账数据２金额（元）
            cell = row.createCell(8);
            cell.setCellValue(autoAccountingExcelRow.getServiceFee().toString());
            //分账数据3金额（元）
            cell = row.createCell(9);
            cell.setCellValue(autoAccountingExcelRow.getEvaluationFee().toString());
            //分账数据4金额（元）
            cell = row.createCell(10);
            cell.setCellValue("");
            //收分账数据２的公司
            cell = row.createCell(11);
            cell.setCellValue(autoAccountingExcelRow.getServiceFeeChargeCompany());
            //持卡人姓名
            cell = row.createCell(12);
            cell.setCellValue("");
            //证件号
            cell = row.createCell(13);
            cell.setCellValue("");
            //扣款结果
            cell = row.createCell(14);
            cell.setCellValue("");
            //原因
            cell = row.createCell(15);
            if (StringUtils.isNotBlank(autoAccountingExcelRow.getReason())){
                cell.setCellValue("自动入账."+autoAccountingExcelRow.getReason());
            }else{
                cell.setCellValue("自动入账.");
            }

            //银联分账数据１扣款(分)
            cell = row.createCell(16);
            cell.setCellValue("");
            //银联分账数据2扣款(分)
            cell = row.createCell(17);
            cell.setCellValue("");
            //是否已核销
            cell = row.createCell(18);
            cell.setCellValue("");
            //是否代扣
            cell = row.createCell(19);
            cell.setCellValue(autoAccountingExcelRow.getIsDeduction());
            //调查评估费入账公司
            cell = row.createCell(20);
            cell.setCellValue(autoAccountingExcelRow.getEvaluationFeeChargeCompany());
            //还款方式
            cell = row.createCell(21);
            cell.setCellValue(autoAccountingExcelRow.getRepaymentMethod());
            i++;
        }
        return workbook;
    }

    /**
     * 上传excel文档到指定路径
     *
     * @param workbook Workbook对象
     * @throws IOException
     */
    public static void upload(Workbook workbook) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook.write(os);
        byte[] content = os.toByteArray();
        InputStream is = new ByteArrayInputStream(content);
        StringBuilder fileName = new StringBuilder("/AutoAccouting/");
        fileName.append(Utility.convertToString(new Date(), "yyyyMMdd_HHmmss"));
        fileName.append(".xls");
        AutoAccountingFtpUtil.upload(fileName.toString(), is);
        try {
            HttpClientUtil.httpGet(environment.getProperty("auto.accounting.execute.url"));
        } catch (IOException e) {
            LOGGER.error("上传入账excel文档[{}]失败",fileName.toString());
            throw e;
        }
    }


}
