package org.starlightfinancial.deductiongateway.web;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.local.MD5Value;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.service.ReconciliationService;
import org.starlightfinancial.deductiongateway.service.SystemService;
import org.starlightfinancial.deductiongateway.service.impl.ScheduledTaskService;
import org.starlightfinancial.deductiongateway.utility.CalMD5;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 代扣账户数据管理Controller
 */
@Controller
public class MortgageDeductionController {

    private static final Logger log = LoggerFactory.getLogger(MortgageDeductionController.class);

    @Autowired
    private MortgageDeductionService mortgageDeductionService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ReconciliationService reconciliationService;
    @Autowired
    private ScheduledTaskService scheduledTaskService;


    /**
     * 代扣账户数据文件导入
     *
     * @param uploadFile
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/upload.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> importFile(MultipartFile uploadFile, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        String md5ByFile = CalMD5.getMd5ByFile(uploadFile);
        List<MD5Value> md5ValueList = systemService.findAllMD5();
        List<String> list = new ArrayList<>();
        for (MD5Value md5Value : md5ValueList) {
            list.add(md5Value.getMd5());
        }
        TreeSet<String> treeSet = new TreeSet<>(new Comparator<String>() {
            @Override
            public int compare(String oldString, String newString) {
                if (oldString.equals(newString)) {
                    return 0;
                }
                return 1;
            }
        });
        treeSet.addAll(list);
        boolean isAdd = treeSet.add(md5ByFile);
        if (!isAdd) {
            map.put("result", "0");
            map.put("msg", "此文件已上传");
            return map;
        }
        systemService.saveMD5(md5ByFile);
        mortgageDeductionService.importCustomerData(uploadFile, Utility.getLoginUserId(session));
        map.put("result", "1");
        map.put("msg", uploadFile.getOriginalFilename());
        return map;
    }

    /**
     * 查询代扣数据
     *
     * @param startDate
     * @param endDate
     * @param customerName
     * @param pageBean
     * @param type         0:已执行代扣的数据 1:未执行
     * @param contractNo   合同编号
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/queryDeductionData.do")
    @ResponseBody
    public Map<String, Object> queryDeductionData(Date startDate, Date endDate, String customerName, PageBean pageBean, String type, String contractNo, HttpSession session) {
        endDate = Utility.toMidNight(endDate);
        PageBean result = mortgageDeductionService.queryMortgageDeductionData(startDate, endDate, customerName.trim(), pageBean, type, contractNo.trim(), Utility.getLoginUserId(session));
        return Utility.pageBean2Map(pageBean);
    }


    /**
     * 导出代扣结果Excel
     *
     * @param startDate
     * @param endDate
     * @param customerName
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/mortgageDeductionController/exportXLS.do")
    public void exportXLS(Date startDate, Date endDate, String customerName, HttpServletResponse response) throws IOException {
        endDate = Utility.toMidNight(endDate);
        Workbook workbook = mortgageDeductionService.exportXLS(startDate, endDate, customerName.trim());
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String fileName = "扣款结果统计报表" + format.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(outputStream);

    }


    /**
     * 删除记录
     *
     * @param ids 记录id
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/deleteMortgageDeductions")
    @ResponseBody
    public String deleteMortgageDeductions(String ids) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return "请选择一条记录进行删除";
            }
            List<MortgageDeduction> list = mortgageDeductionService.findMortgageDeductionListByIds(ids);
            if (list != null) {
                list.get(0).setIssuccess("1");
                mortgageDeductionService.deleteMortgageDeductions(list);
                return "1";
            } else {
                return "0";
            }
        } catch (Exception e) {
            log.debug("删除代扣记录失败", e);
            return "0";
        }
    }



    /**
     * 手动触发批量代扣
     *
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/manualBatchDeduction.do")
    @ResponseBody
    public String manualBatchDeduction() {

        try {
            scheduledTaskService.execute();
            return "1";
        } catch (Exception e) {
            log.debug("手动触发批量代扣失败", e);
            return "0";
        }
    }

    /**
     * 手动触发代扣入账文件上传
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/uploadAutoAccountingFile.do")
    @ResponseBody
    public String uploadAutoAccountingFile() {
        try {
            mortgageDeductionService.uploadAutoAccountingFile();
            return "1";
        } catch (IOException | ClassNotFoundException e) {
            log.error("**********处理代扣自动入账excel文档深复制异常**********", e);
            return "0";
        }
    }


}
