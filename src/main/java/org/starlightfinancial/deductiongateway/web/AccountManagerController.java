package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.Map;

/**
 * 卡号管理Controller
 */
@Controller
@RequestMapping(value = "/accountManagerController")
public class AccountManagerController {

    private static final Logger log = LoggerFactory.getLogger(AccountManagerController.class);

    @Autowired
    private AccountManagerService accountManagerService;

    /**
     * 根据条件查询卡号
     *
     * @return
     */
    @RequestMapping(value = "/queryAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean) {
        PageBean result =  accountManagerService.queryAccount(contractNo,bizNo,accountName,pageBean);
        return Utility.pageBean2Map(result);
    }


    @RequestMapping(value="/updateAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String  updateAccount (AccountManager accountManager){
        try {
            accountManagerService.updateAccount(accountManager);
            return "1";
        } catch (Exception e) {
            log.debug("更新卡号管理记录失败",e);
            return "0";
        }
    }
}
