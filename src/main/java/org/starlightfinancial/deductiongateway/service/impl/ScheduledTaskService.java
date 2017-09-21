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
    @Qualifier("accountAutoBatchImport")
    Job accountAutoBatchImport;

    public JobParameters jobParameter;

    @Scheduled(cron = "00 50 08 * * ? ")
    public void execute() throws Exception {
        System.out.println("执行了吗");
        jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(autoDeduction, jobParameter);
    }

    @Scheduled(cron = "00 20 04 * * ? ")
    public void executeAccountAutoBatchImport() throws Exception {
        System.out.println("执行了吗");
        jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(accountAutoBatchImport, jobParameter);

    }
}
