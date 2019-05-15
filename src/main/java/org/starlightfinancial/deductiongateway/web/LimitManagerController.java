package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.service.LimitManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.Map;

/**
 * 限额管理Controller
 */
@Controller
@RequestMapping(value = "/limitManagerController")
public class LimitManagerController {

    private static final Logger log = LoggerFactory.getLogger(LimitManagerController.class);
    @Autowired
    private LimitManagerService limitManagerService;


    /**
     * 条件查询记录
     * @param pageBean 分页参数
     * @param limitManager 包含了查询条件
     * @return
     */
    @RequestMapping(value = "/queryLimit.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryLimit(PageBean pageBean, LimitManager limitManager) {
        PageBean result = limitManagerService.queryLimit(pageBean, limitManager);
        return Utility.pageBean2Map(result);
    }


    /**
     * 保存或更新记录
     * @param limitManager
     * @return
     */
    @RequestMapping(value = "/saveOrUpdateLimit.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveOrUpdateLimit(LimitManager limitManager) {
        try {
            limitManagerService.saveOrUpdateLimit(limitManager);
            return "1";
        } catch (Exception e) {
            log.debug("更新/保存限额管理记录失败", e);
            return "0";
        }
    }

    /**
     * 判断传入的银行在传入渠道是否存在限额记录
     * @param bankCode 银行编码
     * @param channel 渠道
     * @return
     */
    @RequestMapping(value = "/isExisted.do")
    @ResponseBody
    public Message isExisted(String bankCode, String channel) {
        boolean existed = limitManagerService.isExisted(bankCode, channel);
        if (existed) {
            return Message.fail("不能进行添加操作,当前银行在当前渠道已存在");
        } else {
            return Message.success();
        }

    }


}
