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

    @Scheduled(cron = "00 50 08 * * ? ")
    public void execute() throws Exception {
        System.out.println("执行了吗");

        //下面是判断是否当天在轧账时间内:从当月最后一天开始判断是否是节假日,如果是节假日将最后一天添加到集合中,
        // 往前推算一天判断是否是节假日,如果是添加到集合中,依次类推直到不是节假日,获取到了轧账时间的最早的一天.通过
        //判断当天是否在轧账的
        LocalDate now = LocalDate.now();
        //
        LocalDate firstDay = null;
        LocalDate lastDay = now.with(TemporalAdjusters.lastDayOfMonth());
        while (true) {
            String autoSwitch = isHoliday(lastDay);
            if (StringUtils.equals("0", autoSwitch)) {
                firstDay = lastDay;
            } else {
                break;
            }
            lastDay = lastDay.minusDays(1);
        }
        System.out.println(lastDay);

        boolean afterOrEqual = now.isAfter(firstDay) || now.isEqual(firstDay);
        if (afterOrEqual) {
            //如果是轧账期间,不执行自动代扣
            return;
        } else {
            //如果是在轧账时间前,执行
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
        //holiday为空表示非节假日,开启付易贷自动代扣
        if (holiday == null) {
            autoSwitch = "1";
        }
        return autoSwitch;
    }


}
