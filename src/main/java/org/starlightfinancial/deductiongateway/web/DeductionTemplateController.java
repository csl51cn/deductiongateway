package org.starlightfinancial.deductiongateway.web;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.service.DeductionTemplateService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 代扣模板管理Controller
 */
@Controller
@RequestMapping(value = "/deductionTemplateController")
public class DeductionTemplateController {


    @Autowired
    private DeductionTemplateService deductionTemplateService;

    @RequestMapping(value = "/queryDeductionTemplate.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryDeductionTemplate(PageBean pageBean, String isSuccess, String contractNo, String bizNo, Date startDate, Date endDate) {
        endDate = Utility.toMidNight(endDate);
        PageBean result = deductionTemplateService.queryDeductionTemplate(pageBean, isSuccess,contractNo.trim(),bizNo.trim(),startDate,endDate);
        return Utility.pageBean2Map(result);
    }


    @RequestMapping(value = "/exportXLS.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportXLS(String isSuccess, String contractNo, String bizNo, Date startDate, Date endDate, HttpServletResponse response) throws IOException {
        endDate = Utility.toMidNight(endDate);
        Workbook workbook = deductionTemplateService.exportXLS(isSuccess,contractNo.trim(),bizNo.trim(),startDate,endDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String fileName = "代扣模板" + format.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(outputStream);
    }







}
