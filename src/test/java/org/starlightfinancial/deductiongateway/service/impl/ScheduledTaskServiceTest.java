package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * Created by sili.chen on 2018/1/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Rollback
public class ScheduledTaskServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskServiceTest.class);
    @Autowired
    ScheduledTaskService scheduledTaskService;
//
//    @Test
//    public void execute() throws Exception {
//        scheduledTaskService.execute();
//    }
//
//    @Test
//    public void deductionTemplateImport() throws Exception {
//        scheduledTaskService.deductionTemplateImport();
//    }


    @Test
    public void testLocalDate() throws Exception {
        //轧账后不进行代扣,下面是判断是否当天在不进行代扣的时间段内:从当月最后一天开始判断是否是节假日,如果是节假日将最后一天保存到firstDay中,
        // 往前推算一天判断是否是节假日,如果是就保存到firstDay中,依次类推直到不是节假日,获取到了轧账后不代扣的第一天.通过
        //判断当天是否>=不代扣的最早一天,如果成立,不进行自动代扣,如果不成立进行自动代扣
        LocalDate now = LocalDate.parse("2018-04-30");
        LocalDate firstDay = null;
        //首先获取到当月最后一天
        LocalDate tempDay = now.with(TemporalAdjusters.lastDayOfMonth());
        while (true) {
            String autoSwitch = scheduledTaskService.isHoliday(tempDay);
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
            //如果是轧账期间,不执行自动代扣
            LOGGER.info("今天{} 处于轧账时间后不自动代扣",now.toString());
        } else {
            //如果是在轧账时间前,执行自动代扣
            LOGGER.info("今天{}执行自动代扣",now.toString());
        }
    }
}