package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @RequestMapping(value = "/queryLimit.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryLimit(PageBean pageBean) {
        PageBean result = limitManagerService.queryAllLimit(pageBean);
        return Utility.pageBean2Map(result);
    }


    @RequestMapping(value = "/saveOrUpdateLimit.do",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveOrUpdateLimit(LimitManager limitManager) {
        try {
            limitManagerService.saveOrUpdateLimit(limitManager);
            return "1";
        } catch (Exception e) {
            log.debug("更新/保存限额管理记录失败",e);
            return "0";
        }
    }


}
