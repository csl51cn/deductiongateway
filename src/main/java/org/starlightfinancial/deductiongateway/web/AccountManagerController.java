package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 卡号管理Controller
 *
 * @author senlin.deng
 */
@Controller
@RequestMapping(value = "/accountManagerController")
public class AccountManagerController {

    private static final Logger log = LoggerFactory.getLogger(AccountManagerController.class);

    @Autowired
    private AccountManagerService accountManagerService;

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("accountAutoBatchImport")
    Job accountAutoBatchImport;

    public JobParameters jobParameters;

    /**
     * 根据条件查询卡号
     *
     * @return
     */
    @RequestMapping(value = "/queryAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean) {
        PageBean result = accountManagerService.queryAccount(contractNo.trim(), bizNo.trim(), accountName.trim(), pageBean);
        return Utility.pageBean2Map(result);
    }


    /**
     * @param bizNo
     * @return
     */
    @RequestMapping(value = "/addAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message addAccount(String bizNo) {
        try {
            Message message =   accountManagerService.addAccount(bizNo.trim());
            return message;
        } catch (Exception e) {
            log.debug("添加代扣卡信息失败", e);
            return Message.fail("添加代扣卡信息失败");
        }
    }


    /**
     * 更新记录
     *
     * @param accountManager
     * @return
     */
    @RequestMapping(value = "/updateAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateAccount(AccountManager accountManager) {
        try {
            accountManagerService.updateAccount(accountManager);
            return "1";
        } catch (Exception e) {
            log.debug("更新卡号管理记录失败", e);
            return "0";
        }
    }

    /**
     * 手动执行代扣卡批量导入
     *
     * @return
     */
    @RequestMapping(value = "/executeAccountAutoBatchImport.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String executeAccountAutoBatchImport() {
        List<AccountManager> lastAccount = accountManagerService.findLastAccount();
        String lastLoanDate = null;
        if (lastAccount.size() > 0) {
            AccountManager accountManager = lastAccount.get(0);
            Date loanDate = accountManager.getLoanDate();
            if (loanDate != null) {
                lastLoanDate = Utility.convertToString(loanDate);
            } else {
                lastLoanDate = setLastLoanDate();
            }
        } else {
            lastLoanDate = setLastLoanDate();
        }

        try {
            jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("lastLoanDate", lastLoanDate).toJobParameters();
            jobLauncher.run(accountAutoBatchImport, jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.debug("代扣卡批量导入失败 ", e);
            return "0";
        }
        return "1";
    }

    private String setLastLoanDate() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return yesterday.toString();
    }


    /**
     * 银联-查询是否签约
     *
     * @param id 记录id
     * @return
     */
    @RequestMapping(value = "/unionPayIsSigned.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message unionPayIsSigned(Integer id) {
        Message message = accountManagerService.unionPayIsSigned(id);
        return message;

    }


    /**
     * 银联--发送签约短信验证码
     *
     * @param id 记录id
     * @param account 账户卡号
     * @param certificateType 证件类型
     * @param certificateNo 证件号码
     * @param accountName 账户名
     * @param mobile 手机号
     * @return
     */
    @RequestMapping(value = "/unionPaySendSignSmsCode.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message unionPaySendSignSmsCode(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile) {
        Message message = accountManagerService.unionPaySendSignSmsCode(id, account, certificateType, certificateNo, accountName, mobile);
        return message;
    }

    /**
     * 银联--签约
     *
     * @param id 记录id
     * @param account  账户卡号
     * @param certificateType 证件类型
     * @param certificateNo 证件号码
     * @param accountName 账户名
     * @param mobile 手机号
     * @param smsCode 短信验证码
     * @param merOrderNo 短信验证码对应的订单号
     * @return
     */
    @RequestMapping(value = "/unionPaySign.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message unionPaySign(Integer id, String account, String certificateType, String certificateNo, String accountName, String mobile, String smsCode, String merOrderNo) {
        Message message =accountManagerService.unionPaySign(id,account,certificateType,certificateNo,accountName,mobile,smsCode,merOrderNo);
        return message;
    }


}
