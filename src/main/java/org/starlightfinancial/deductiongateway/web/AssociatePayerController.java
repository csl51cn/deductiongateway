package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.domain.local.AssociatePayer;
import org.starlightfinancial.deductiongateway.service.AssociatePayerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 关联还款人信息管理Controller
 * @date: Created in 2018/7/24 15:58
 * @Modified By:
 */
@Controller
@RequestMapping("/associatePayerController")
public class AssociatePayerController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssociatePayerController.class);
    @Autowired
    private AssociatePayerService associatePayerService;


    /**
     * 根据条件查询记录
     *
     * @param pageBean   分页参数
     * @param contractNo 合同编号
     * @return 查询出的记录
     */
    @RequestMapping("/queryAssociatePayer.do")
    @ResponseBody
    public Map<String, Object> queryAssociatePayer(PageBean pageBean, String contractNo) {
        pageBean = associatePayerService.queryAssociatePayer(pageBean, contractNo);
        return Utility.pageBean2Map(pageBean);
    }


    /**
     * 新增关联还款人信息
     *
     * @param associatePayer 关联还款人信息
     * @param session        会话session
     * @return 添加结果
     */
    @RequestMapping(value = "/saveAssociatePayer.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveAssociatePayer(AssociatePayer associatePayer, HttpSession session) {

        //设置创建时间
        associatePayer.setGmtCreate(new Date());
        //设置修改时间
        associatePayer.setGmtModified(associatePayer.getGmtCreate());
        //设置创建人id
        associatePayer.setCreateId(Utility.getLoginUserId(session));
        //设置修改人id
        associatePayer.setModifiedId(associatePayer.getCreateId());
        try {
            associatePayerService.saveAssociatePayer(associatePayer);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("保存关联还款人信息失败", e);
            return "0";
        }
    }


    /**
     * 更新关联还款人信息
     *
     * @param associatePayer 由页面传入的关联还款人信息
     * @return 返回更新结果
     */
    @RequestMapping(value = "/updateAssociatePayer.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateAssociatePayer(AssociatePayer associatePayer, HttpSession session) {

        //设置修改时间
        associatePayer.setGmtModified(new Date());
        //设置修改人id
        associatePayer.setModifiedId(Utility.getLoginUserId(session));
        try {
            associatePayerService.updateAssociatePayer(associatePayer);
            return "1";
        } catch (Exception e) {
            LOGGER.debug("保存非代扣还款信息失败", e);
            return "0";
        }
    }


}
