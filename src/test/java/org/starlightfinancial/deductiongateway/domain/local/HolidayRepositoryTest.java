package org.starlightfinancial.deductiongateway.domain.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.remote.Holiday;
import org.starlightfinancial.deductiongateway.domain.remote.HolidayRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;

/**
 * Created by senlin.deng on 2017-10-10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
@Rollback(value = false)
public class HolidayRepositoryTest {

    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    public void findByNonOverTime() throws Exception {
        Holiday byNonOverTime = holidayRepository.findByNonOverTime(LocalDate.now().toString());
        System.out.println(byNonOverTime);

    }

}