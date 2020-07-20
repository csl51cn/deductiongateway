package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.remote.Holiday;
import org.starlightfinancial.deductiongateway.domain.remote.HolidayRepository;
import org.starlightfinancial.deductiongateway.service.CacheService;
import org.starlightfinancial.deductiongateway.service.FinancialVoucherService;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Created by sili.chen on 2017/8/23
 */
@Service
@EnableScheduling
public class ScheduledTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskService.class);
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("autoDeduction")
    Job autoDeduction;

    @Autowired
    @Qualifier("deductionTemplatBatchImport")
    Job deductionTemplatBatchImport;

    @Autowired
    @Qualifier("accountAutoBatchImport")
    Job accountAutoBatchImport;


    @Autowired
    private HolidayRepository holidayRepository;

    public JobParameters jobParameters;

    @Autowired
    private MortgageDeductionService mortgageDeductionService;

    @Autowired
    private FinancialVoucherService financialVoucherService;

    /**
     * 自动代扣
     *
     * @throws Exception
     */
    @Scheduled(cron = "00 50 08 * * ? ")
    public void execute() throws Exception {
        LOGGER.info("开始执行自动代扣,[{}]", LocalDate.now().toString());

        //轧账后的月底节假日不进行代扣,下面是判断是否当天在不进行代扣的时间段内:从当月最后一天开始判断是否是节假日,如果是节假日将最后一天保存到firstDay中,
        // 往前推算一天判断是否是节假日,如果是就保存到firstDay中,依次类推直到不是节假日,获取到了轧账后不代扣的第一天.通过
        //判断当天是否>=不代扣的最早一天,如果成立,不进行自动代扣,如果不成立进行自动代扣
        LocalDate now = LocalDate.now();
        LocalDate firstDay = null;
        //首先获取到当月最后一天
        LocalDate tempDay = now.with(TemporalAdjusters.lastDayOfMonth());
        while (true) {
            String autoSwitch = isHoliday(tempDay);
            if (StringUtils.equals("0", autoSwitch)) {
                firstDay = tempDay;
            } else {
                break;
            }
            tempDay = tempDay.minusDays(1);
        }
        boolean afterOrEqual = false;

        if (firstDay != null) {
            afterOrEqual = now.isAfter(firstDay) || now.isEqual(firstDay);
        }
        if (afterOrEqual) {
            //如果是轧账后的月底节假日,不执行自动代扣
            LOGGER.info("今天{} 处于轧账后的月底节假日内不自动代扣", now.toString());
        } else {
            //如果是在轧账时间前,执行自动代扣
            jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
            jobLauncher.run(autoDeduction, jobParameters);
        }
    }


    /**
     * 自动导入当天代扣模板
     *
     * @throws Exception 异常时抛出
     */
    @Scheduled(cron = "00 30 08 * * ? ")
    public void deductionTemplateImport() throws Exception {
        LOGGER.info("自动导入当天代扣模板,[{}]", LocalDate.now().toString());
        String autoSwitch = isHoliday(LocalDate.now());
        jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("autoSwitch", autoSwitch).toJobParameters();
        jobLauncher.run(deductionTemplatBatchImport, jobParameters);
    }

    public String isHoliday(LocalDate localDate) {
        Holiday holiday = holidayRepository.findByNonOverTime(localDate.toString());
        String autoSwitch = "0";
        //holiday为空表示非节假日,开启付易贷日扣自动代扣
        if (holiday == null) {
            autoSwitch = "1";
        }
        return autoSwitch;
    }

    /**
     * 自动上传代扣成功的记录:从12点-23点每小时处理一次
     */
    @Scheduled(cron = "0 55 12-23 * * ?")
    public void uploadAutoAccountingFile() {
        if (!StringUtils.equals(SpringContextUtil.getActiveProfile(), "prod")) {
            //如果不是生产环境,不执行
            return;
        }
        LOGGER.info("**********开始处理代扣自动入账excel文档**********");
        try {
            mortgageDeductionService.uploadAutoAccountingFile();
        } catch (IOException e) {
            LOGGER.error("**********处理代扣自动入账excel文档深复制异常**********", e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("**********处理代扣自动入账excel文档深复制异常**********", e);
        }

    }

    /**
     * 每天上午5点刷新缓存服务
     */
    @Scheduled(cron = "0 0 5 * * ? ")
    public void refreshCacheService() {
        CacheService.refresh();
    }

    /**
     * 导入昨天的还款数据:成功代扣的和非代扣还款的,设置为9:40
     */
    @Scheduled(cron = "00 40 9 * * ? ")
    public void importRepaymentInfo() {
        if (!StringUtils.equals(SpringContextUtil.getActiveProfile(), "prod")) {
            //如果不是生产环境,不执行
            return;
        }
        LOGGER.info("**********开始导入昨天还款信息到业务系统**********");
        try {
            financialVoucherService.importRepaymentData();
            LOGGER.info("**********导入昨天还款信息到业务系统成功**********");
        } catch (Exception e) {
            LOGGER.error("**********导入还款数据到业务系统异常**********", e);
        }
    }


    /**
     * 每天上午9点20导入昨天放款的代扣信息
     * @throws Exception
     */
    @Scheduled(cron = "00 20 09 * * ? ")
    public void executeAccountAutoBatchImport() throws Exception {
        LOGGER.info("**********开始导入昨天放款的代扣卡号**********");
        LocalDate lastLoanDate = LocalDate.now().minusDays(1);
        jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("lastLoanDate", lastLoanDate.toString()).toJobParameters();
        jobLauncher.run(accountAutoBatchImport, jobParameters);
        LOGGER.info("**********导入昨天放款的代扣卡号成功**********");

    }
}
