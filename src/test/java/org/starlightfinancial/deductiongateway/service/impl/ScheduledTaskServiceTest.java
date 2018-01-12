package org.starlightfinancial.deductiongateway.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by sili.chen on 2018/1/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Rollback
public class ScheduledTaskServiceTest {

    @Autowired
    ScheduledTaskService scheduledTaskService;

    @Test
    public void execute() throws Exception {
        scheduledTaskService.execute();
    }

    @Test
    public void deductionTemplateImport() throws Exception {
        scheduledTaskService.deductionTemplateImport();
    }

}