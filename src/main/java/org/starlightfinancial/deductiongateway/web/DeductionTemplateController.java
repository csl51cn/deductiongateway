package org.starlightfinancial.deductiongateway.web;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.service.DeductionTemplateService;
import org.starlightfinancial.deductiongateway.service.impl.ScheduledTaskService;
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

    private static final Logger log = LoggerFactory.getLogger(DeductionTemplateController.class);

    @Autowired
    private DeductionTemplateService deductionTemplateService;

    @Autowired
    private ScheduledTaskService scheduledTaskService;

    /**
     * 查询代扣模板
     * @param pageBean
     * @param isSuccess  金额是否代扣完,0:还有金额未代扣,1:金额已经扣完
     * @param contractNo    合同编号
     * @param customerName  客户名称
     * @param startDate
     * @param endDate
     * @return
     */
    @RequestMapping(value = "/queryDeductionTemplate.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryDeductionTemplate(PageBean pageBean, String isSuccess, String contractNo, String customerName, Date startDate, Date endDate) {
        endDate = Utility.toMidNight(endDate);
        PageBean result = deductionTemplateService.queryDeductionTemplate(pageBean, isSuccess,contractNo.trim(),customerName.trim(),startDate,endDate);
        return Utility.pageBean2Map(result);
    }

    /**
     * 导出代扣模板
     * @param isSuccess
     * @param contractNo
     * @param customerName
     * @param startDate
     * @param endDate
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/exportXLS.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportXLS(String isSuccess, String contractNo, String customerName, Date startDate, Date endDate, HttpServletResponse response) throws IOException {
        endDate = Utility.toMidNight(endDate);
        Workbook workbook = deductionTemplateService.exportXLS(isSuccess,contractNo.trim(),customerName.trim(),startDate,endDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String fileName = "代扣模板" + format.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * 手动触发导入当日代扣模板
     *
     * @return
     */
    @RequestMapping(value = "/manualImportTemplate.do")
    @ResponseBody
    public  String  manualImportTemplate(){
        try {
            scheduledTaskService.deductionTemplateImport();
            return "1";
        }catch (Exception e){
            return "0";
        }

    }










}
