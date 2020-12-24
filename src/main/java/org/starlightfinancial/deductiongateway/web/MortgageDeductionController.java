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
import org.starlightfinancial.deductiongateway.vo.MortgageDeductionQueryCondition;

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
     * @param mortgageDeductionQueryCondition 查询条件
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/queryDeductionData.do")
    @ResponseBody
    public Map<String, Object> queryDeductionData(MortgageDeductionQueryCondition mortgageDeductionQueryCondition, HttpSession session) {
        mortgageDeductionQueryCondition.setEndDate(Utility.toMidNight(mortgageDeductionQueryCondition.getEndDate()));
        mortgageDeductionQueryCondition.setContractNo(mortgageDeductionQueryCondition.getContractNo().trim());
        mortgageDeductionQueryCondition.setCustomerName(mortgageDeductionQueryCondition.getCustomerName().trim());
        PageBean result = mortgageDeductionService.queryMortgageDeductionData(mortgageDeductionQueryCondition, Utility.getLoginUserId(session));
        return Utility.pageBean2Map(result);
    }


    /**
     * 导出代扣结果Excel
     *
     * @param mortgageDeductionQueryCondition 查询条件
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/mortgageDeductionController/exportXLS.do")
    public void exportXLS(MortgageDeductionQueryCondition mortgageDeductionQueryCondition, HttpServletResponse response) throws IOException {
        mortgageDeductionQueryCondition.setEndDate(Utility.toMidNight(mortgageDeductionQueryCondition.getEndDate()));
        mortgageDeductionQueryCondition.setContractNo(mortgageDeductionQueryCondition.getContractNo().trim());
        mortgageDeductionQueryCondition.setCustomerName(mortgageDeductionQueryCondition.getCustomerName().trim());
        Workbook workbook = mortgageDeductionService.exportXLS(mortgageDeductionQueryCondition);
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddss");
        String fileName = "扣款结果统计报表" + format.format(new Date());
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(outputStream);

    }

    /**
     * 人工确认扣款成功
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/updateMortgageDeductions.do")
    @ResponseBody
    public String updateMortgageDeductions(String ids) {
        try {
            List<MortgageDeduction> list = mortgageDeductionService.findMortgageDeductionListByIds(ids);
            if (list != null) {
                list.get(0).setIssuccess("1");
                list.get(0).setErrorResult("成功");
                mortgageDeductionService.updateMortgageDeductions(list);
                return "1";
            } else {
                return "0";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
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
     *
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
