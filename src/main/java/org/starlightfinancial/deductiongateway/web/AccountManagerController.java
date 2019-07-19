package org.starlightfinancial.deductiongateway.web;

import org.apache.poi.util.IOUtils;
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
import org.starlightfinancial.deductiongateway.vo.AccountManagerVO;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    public Map<String, Object> queryAccount(AccountManagerVO accountManagerVO, PageBean pageBean) {
        PageBean result = accountManagerService.queryAccount(accountManagerVO, pageBean);
        return Utility.pageBean2Map(result);
    }


    /**
     * 增加代扣卡
     *
     * @param bizNo 业务号
     * @return
     */
    @RequestMapping(value = "/addAccount.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message addAccount(String bizNo) {
        try {
            Message message = accountManagerService.addAccount(bizNo.trim());
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
        AccountManager accountManager = accountManagerService.findLastAccount();
        String lastLoanDate = null;
        if (Objects.nonNull(accountManager)) {
            Date loanDate = accountManager.getLoanDate();
            if (loanDate != null) {
                lastLoanDate = Utility.convertToString(loanDate, "yyyy-MM-dd");
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
     * 根据条件查询,生成白名单,压缩导出
     */
    @RequestMapping(value = "/whiteListExport.do")
    public void whiteListExport(AccountManagerVO accountManagerVO, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("application/zip;");
        response.setHeader("Content-Disposition", "attachment;filename="
                + new String(("白名单导出" + Utility.getTimestamp()).getBytes("gb2312"), "iso8859-1") + ".zip");

        // 白名单文件名与内容的映射
        Map<String, String> whiteListContent = accountManagerService.whiteListExport(accountManagerVO);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        CheckedOutputStream cos = new CheckedOutputStream(output, new CRC32());
        ZipOutputStream zos = new ZipOutputStream(cos);
        for (Map.Entry<String, String> entry : whiteListContent.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            //构建输入流
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(content.getBytes()));
            //创建文件（zip里面的文件）
            ZipEntry zipEntry = new ZipEntry(fileName);
            //放入文件
            zos.putNextEntry(zipEntry);
            //写入文件
            IOUtils.copy(bis, zos);
            //关闭流
            bis.close();
            zos.closeEntry();
        }

        zos.close();
        //设置返回信息
        response.setHeader("Content-Length", String.valueOf(output.size()));
        IOUtils.copy(new ByteArrayInputStream(output.toByteArray()), response.getOutputStream());
        //创建完压缩文件后关闭流
        cos.close();
        output.close();

    }
}
