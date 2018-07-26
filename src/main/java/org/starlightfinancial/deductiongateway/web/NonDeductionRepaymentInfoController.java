package org.starlightfinancial.deductiongateway.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.common.SameUrlData;
import org.starlightfinancial.deductiongateway.domain.local.NonDeductionRepaymentInfo;
import org.starlightfinancial.deductiongateway.service.NonDeductionRepaymentInfoService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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


    /**
     * 非代扣还款信息文件导入
     *
     * @param uploadFile
     * @param session
     * @return
     */
    @RequestMapping(value = "/upload.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> importFile(MultipartFile uploadFile, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>(2);
        try {
            nonDeductionRepaymentInfoService.importFile(uploadFile, session);
        } catch (Exception e) {
            LOGGER.error("处理非代扣还款信息文件导入异常", e);
            map.put("result", "0");
            map.put("msg", "处理非代扣还款信息文件导入异常");
        }
        map.put("result", "1");
        map.put("msg", uploadFile.getOriginalFilename());
        return map;
    }


    /**
     * 根据条件查询记录
     *
     * @param pageBean     页面参数对象
     * @param startDate    还款开始时间
     * @param endDate      还款结束时间
     * @param customerName 客户名称
     * @param contractNo   合同号
     * @return 返回根据条件查询到的记录
     */
    @RequestMapping(value = "/queryNonDeductionRepaymentInfo.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryNonDeductionRepaymentInfo(PageBean pageBean, Date startDate, Date endDate, String customerName, String contractNo, String isIntegrated) {
        PageBean result = nonDeductionRepaymentInfoService.queryNonDeductionRepaymentInfo(pageBean, startDate, endDate, customerName, contractNo, isIntegrated);
        return Utility.pageBean2Map(result);
    }


    /**
     * 新增非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo 由页面传入的非代扣还款信息
     * @param session                   会话session
     * @return
     */
    @RequestMapping(value = "/saveNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {
        //设置信息是否完整,判断标准:是否有有对应的dateId
        nonDeductionRepaymentInfo.setIsIntegrated(String.valueOf(0));
        //设置是否已上传自动入账文件
        nonDeductionRepaymentInfo.setIsUploaded(String.valueOf(0));
        //设置创建时间
        nonDeductionRepaymentInfo.setGmtCreate(new Date());
        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
        //设置创建人id
        nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
        try {
            nonDeductionRepaymentInfoService.saveNonDeduction(nonDeductionRepaymentInfo);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("保存非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 更新非代扣还款信息
     *
     * @param nonDeductionRepaymentInfo 由页面传入的非代扣还款信息
     * @return
     */
    @RequestMapping(value = "/updateNonDeduction.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateNonDeduction(NonDeductionRepaymentInfo nonDeductionRepaymentInfo, HttpSession session) {

        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(new Date());
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(Utility.getLoginUserId(session));
        try {
            nonDeductionRepaymentInfoService.updateNonDeduction(nonDeductionRepaymentInfo);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("保存非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 上传自动入账excel文件
     *
     * @param ids 一个或多个记录的id
     * @return
     */
    @RequestMapping(value = "/uploadAutoAccountingFile.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    @SameUrlData
    public String uploadAutoAccountingFile(String ids, HttpSession session) {
        if (StringUtils.isEmpty(ids)) {
            return "请选择一条记录上传";
        }
        try {
            nonDeductionRepaymentInfoService.uploadAutoAccountingFile(ids, session);
            return "1";
        } catch (IOException e) {
            e.printStackTrace();
            return "0";
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
        nonDeductionRepaymentInfo.setIsUploaded(String.valueOf(0));
        //设置创建时间
        nonDeductionRepaymentInfo.setGmtCreate(new Date());
        //设置修改时间
        nonDeductionRepaymentInfo.setGmtModified(nonDeductionRepaymentInfo.getGmtCreate());
        //设置创建人id
        nonDeductionRepaymentInfo.setCreateId(Utility.getLoginUserId(session));
        //设置修改人id
        nonDeductionRepaymentInfo.setModifiedId(nonDeductionRepaymentInfo.getCreateId());
        try {
            nonDeductionRepaymentInfoService.splitNonDeduction(nonDeductionRepaymentInfo, originalNonDeductionRepaymentInfoId);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("拆分非代扣还款信息失败", e);
            return "0";
        }
    }


    /**
     * 刷新CacheService,重新尝试查找匹配的业务信息
     *
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param session   会话session
     * @return 执行结果
     */
    @RequestMapping(value = "/retrySearchBusinessTransactionInfo.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String retrySearchBusinessTransactionInfo(Date startDate, Date endDate, HttpSession session) {
        try {
            nonDeductionRepaymentInfoService.retrySearchBusinessTransactionInfo(startDate,endDate,session);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("重新尝试查找匹配的业务信息失败", e);
            return "0";
        }
    }


}
