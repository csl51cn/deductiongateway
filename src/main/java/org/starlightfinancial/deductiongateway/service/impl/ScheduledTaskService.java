package org.starlightfinancial.deductiongateway.service.impl;

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

/**
 * Created by sili.chen on 2017/8/23
 */
@Service
@EnableScheduling
public class ScheduledTaskService {
    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    @Qualifier("autoDeduction")
    Job autoDeduction;

    @Autowired
    private HolidayRepository holidayRepository;

    public JobParameters jobParameters;

    @Scheduled(cron = "00 50 08 * * ? ")
    public void execute() throws Exception {
        System.out.println("执行了吗");
        Holiday holiday = holidayRepository.findByNonOverTime(LocalDate.now().toString());
        String autoSwitch = null;
        if (holiday == null) { //holiday为空表示非节假日,开启付易贷自动代扣
            autoSwitch = "1";
        } else {
            autoSwitch = "0";
        }
        jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).addString("autoSwitch", autoSwitch).toJobParameters();
        jobLauncher.run(autoDeduction, jobParameters);
    }


}
