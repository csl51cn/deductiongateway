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
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfoQueryCondition;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.exception.nondeduction.FieldFormatCheckException;
import org.starlightfinancial.deductiongateway.service.CacheService;
import org.starlightfinancial.deductiongateway.service.FinancialVoucherService;
import org.starlightfinancial.deductiongateway.service.NonDeductionRepaymentInfoService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: Senlin.Deng
 * @Description: 非代扣还款信息Controller
 * @date: Created in 2018/7/17 17:12
 * @Modified By:
 */
@Controller
@RequestMapping(value = "/nonDeductionController")
public class NonDeductionRepaymentInfoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(NonDeductionRepaymentInfoController.class);
    @Autowired
    private NonDeductionRepaymentInfoService nonDeductionRepaymentInfoService;
    @Autowired
    private FinancialVoucherService financialVoucherService;

    /**
     * 非代扣还款信息文件导入
     *
     * @param uploadFile excel文档
     * @param session    会话session
     * @return 上传结果
     */
    @RequestMapping(value = "/upload.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> importFile(MultipartFile uploadFile, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>(2);
        try {
            nonDeductionRepaymentInfoService.importFile(uploadFile, session);
            map.put("result", "1");
            map.put("msg", uploadFile.getOriginalFilename());
        } catch (Exception e) {
            LOGGER.error("处理非代扣还款信息文件导入异常", e);
            map.put("result", "0");
            map.put("msg", "处理非代扣还款信息文件导入异常");
        }
        return map;
    }


    /**
     * 根据条件查询记录
     *
     * @param pageBean                                页面参数对象
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @return 返回根据条件查询到的记录
     */
    @RequestMapping(value = "/queryNonDeductionRepaymentInfo.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryNonDeductionRepaymentInfo(PageBean pageBean, NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpSession session) {
        PageBean result = nonDeductionRepaymentInfoService.queryNonDeductionRepaymentInfo(pageBean, nonDeductionRepaymentInfoQueryCondition, session);
        return Utility.pageBean2Map(result);
    }


    /**
     * 新增非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo 由页面传入的非代扣还款信息
     * @param session                   会话session
     * @return 返回操作结果
     */
    @RequestMapping(value = "/saveNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {
        //设置信息是否完整,判断标准:是否有有对应的dateId
        nonDeductionRepaymentInfo.setIsIntegrated(ConstantsEnum.FAIL.getCode());
        //设置是否已上传自动入账文件
        nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.FAIL.getCode());
        //设置创建时间
        nonDeductionRepaymentInfo.setGmtCreate(new Date());
        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
        //设置创建人id
        nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
        //设置入账时间,默认是还款时间
        nonDeductionRepaymentInfo.setAccountingDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
        //设置是否是系统自动匹配业务信息为否
        nonDeductionRepaymentInfo.setIsAutoMatched(ConstantsEnum.FAIL.getCode());
        try {
            nonDeductionRepaymentInfoService.saveNonDeduction(nonDeductionRepaymentInfo, session);
            return "1";
        } catch (Exception e) {
            LOGGER.error("保存非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 更新非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo 由页面传入的非代扣还款信息
     * @return 返回操作结果
     */
    @RequestMapping(value = "/updateNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {

        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(new Date());
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
        try {
            nonDeductionRepaymentInfoService.updateNonDeduction(nonDeductionRepaymentInfo, session);
            return "1";
        } catch (NumberFormatException e) {
            LOGGER.error("保存非代扣还款信息失败,数字格式化失败", e);
            return "数字格式不正确";
        } catch (Exception e) {
            LOGGER.error("保存非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 上传自动入账excel文件
     *
     * @param ids 一个或多个记录的id
     * @return 返回操作结果
     */
    @RequestMapping(value = "/uploadAutoAccountingFile.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String uploadAutoAccountingFile(String ids, HttpSession session) {
        if (StringUtils.isEmpty(ids)) {
            return "请选择一条记录上传";
        }
        try {
            List<NonDeductionRepaymentInfo> nonDeductionRepaymentInfos = nonDeductionRepaymentInfoService.listNonDeductions(ids);
            //判断是否存在已上传的记录
            boolean anyMatch = nonDeductionRepaymentInfos.stream().anyMatch(nonDeductionRepaymentInfo -> StringUtils.equals(nonDeductionRepaymentInfo.getIsUploaded(), ConstantsEnum.SUCCESS.getCode()));
            if(anyMatch){
                return "选中了包含已上传的记录";
            }
            nonDeductionRepaymentInfoService.uploadAutoAccountingFile(nonDeductionRepaymentInfos, session);
            return "1";
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.error("上传自动入账excel文件失败", e);
            return "上传自动入账excel文件失败";
        } catch (FieldFormatCheckException e) {
            LOGGER.error("{}", e.getMessage());
            return e.getMessage() + ",请修改后重试";
        }
    }


    /**
     * 拆分非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo           由页面传入的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     * @return 拆分结果
     */
    @RequestMapping(value = "/splitNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String splitNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, Long originalNonDeductionRepaymentInfoId, HttpSession session) {
        //设置是否已上传自动入账文件
        nonDeductionRepaymentInfo.setIsUploaded(ConstantsEnum.FAIL.getCode());
        //设置创建时间
        nonDeductionRepaymentInfo.setGmtCreate(new Date());
        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
        //设置创建人id
        nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
        //设置入账日期
        nonDeductionRepaymentInfo.setAccountingDate(nonDeductionRepaymentInfo.getRepaymentTermDate());
        //设置被拆分记录的Id
        nonDeductionRepaymentInfo.setOriginalId(originalNonDeductionRepaymentInfoId);
        try {
            nonDeductionRepaymentInfoService.splitNonDeduction(Stream.of(nonDeductionRepaymentInfo).collect(Collectors.toList()), originalNonDeductionRepaymentInfoId, session);
            return "1";
        } catch (Exception e) {
            LOGGER.error("拆分非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 刷新CacheService,重新尝试查找匹配的业务信息
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @param session                                 会话session
     * @return 执行结果
     */
    @RequestMapping(value = "/retrySearchBusinessTransactionInfo.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String retrySearchBusinessTransactionInfo(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpSession session) {
        try {
            nonDeductionRepaymentInfoService.retrySearchBusinessTransactionInfo(nonDeductionRepaymentInfoQueryCondition, session);
            return "1";
        } catch (Exception e) {
            LOGGER.error("重新尝试查找匹配的业务信息失败", e);
            return "0";
        }
    }

    /**
     * 批量拆分非代扣还款信息
     *
     * @param uploadFile                          由页面传入的非代扣还款信息
     * @param originalNonDeductionRepaymentInfoId 被拆分的非代扣还款信息的id
     * @param session                             会话session
     * @return 拆分结果
     */
    @RequestMapping(value = "/batchSplitNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map batchSplitNonDeduction(MultipartFile uploadFile, Long originalNonDeductionRepaymentInfoId, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>(2);
        try {
            nonDeductionRepaymentInfoService.batchSplitNonDeduction(uploadFile, originalNonDeductionRepaymentInfoId, session);
            map.put("result", "1");
            map.put("msg", uploadFile.getOriginalFilename());
        } catch (Exception e) {
            LOGGER.error("批量拆分非代扣还款信息失败", e);
            map.put("result", "0");
            map.put("msg", uploadFile.getOriginalFilename());
        }

        return map;
    }

    /**
     * 根据条件导出非代扣还款数据
     *
     * @param nonDeductionRepaymentInfoQueryCondition 查询条件
     * @param response                                响应
     * @throws IOException 异常时抛出
     */
    @RequestMapping(value = "/exportXLS.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void exportXLS(NonDeductionRepaymentInfoQueryCondition nonDeductionRepaymentInfoQueryCondition, HttpServletResponse response) throws IOException {

        Workbook workbook = nonDeductionRepaymentInfoService.exportXLS(nonDeductionRepaymentInfoQueryCondition);

        String fileName = "非代扣还款数据" + Utility.convertToString(new Date(), "yyyyMMdd_HH_mm_ss");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "iso8859-1") + ".xls");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    /**
     * 获取非代扣还款数据的总额,此处session保存的最近一次请求的总额
     */
    @RequestMapping(value = "/queryTotalRepaymentAmount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message queryTotalRepaymentAmount(HttpSession session) {
        try {
            String totalRepaymentAmount = (String) session.getAttribute("totalRepaymentAmount");
            return Message.success(totalRepaymentAmount);
        } catch (Exception e) {
            e.printStackTrace();
            return Message.fail();
        }
    }


    /**
     * 将还款信息导入到业务系统
     *
     * @return 导入结果
     */
    @RequestMapping(value = "repaymentInfoImport.do")
    @ResponseBody
    public String repaymentInfoImportToBizSystem() {
        //刷新缓存
        CacheService.refresh();
        try {
            financialVoucherService.importRepaymentData();
            return "1";
        } catch (Exception e) {
            LOGGER.error("手动导入还款信息失败", e);
        }
        return "0";
    }


    /**
     * 修改上传入账文件状态
     *
     * @param ids 一个或多个记录的id
     * @return 返回操作结果
     */
    @RequestMapping(value = "/modifyUploadStatus.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String modifyUploadStatus(String ids, HttpSession session) {
        if (StringUtils.isEmpty(ids)) {
            return "请选择一条记录上传";
        }
        try {
            nonDeductionRepaymentInfoService.modifyUploadStatus(ids, session);
            return "1";
        } catch (Exception e) {
            LOGGER.error("修改上传入账文件状态失败", e);
            return "修改上传入账文件状态";
        }
    }
}
