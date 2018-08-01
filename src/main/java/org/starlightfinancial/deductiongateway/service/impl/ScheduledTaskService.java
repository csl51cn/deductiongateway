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
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;

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
    private HolidayRepository holidayRepository;

    public JobParameters jobParameters;

    @Autowired
    private MortgageDeductionService mortgageDeductionService;

    @Scheduled(cron = "00 50 08 * * ? ")
    public void execute() throws Exception {
        System.out.println("执行了吗");

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
        boolean afterOrEqual =false;

        if (firstDay != null){
            afterOrEqual = now.isAfter(firstDay) || now.isEqual(firstDay);
        }
        if (afterOrEqual) {
            //如果是轧账后的月底节假日,不执行自动代扣
            LOGGER.info("今天{} 处于轧账后的月底节假日内不自动代扣",now.toString());
        } else {
            //如果是在轧账时间前,执行自动代扣
            String autoSwitch = isHoliday(now);
            jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("autoSwitch", autoSwitch).toJobParameters();
            jobLauncher.run(autoDeduction, jobParameters);
        }
    }


    @Scheduled(cron = "00 30 08 * * ? ")
    public void deductionTemplateImport() throws Exception {
        System.out.println("自动导入当天代扣模板");
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
     * 自动上传代扣成功的记录:从13点-21点每小时处理一次
     */
//    @Scheduled(cron = "0 0 13-21 * * ?")
    @Scheduled(cron = "0 13 17 * * ?")
    public void uploadAutoAccountingFile(){
        LOGGER.info("开始处理自动入账excel文档");
        mortgageDeductionService.uploadAutoAccountingFile();
    }

    /**
     * 每天上午5点刷新缓存服务
     */
    @Scheduled(cron = "0 0 5 * * ? ")
    public void refreshCacheService(){
        CacheService.refresh();
    }

}
