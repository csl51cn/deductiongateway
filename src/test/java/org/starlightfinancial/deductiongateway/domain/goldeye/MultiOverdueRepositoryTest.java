package org.starlightfinancial.deductiongateway.domain.goldeye;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/19 10:54
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MultiOverdueRepositoryTest {

    @Autowired
    private MultiOverdueRepository multiOverdueRepository;


    @Test
    public void findByDateId(){
        Date date = new Date();
        List<MultiOverdue> byDateId = multiOverdueRepository.findByDateIdAndPlanTermDateLessThanEqualAndOverdueDaysGreaterThanOrderByPlanTermDateAsc(1306074,date,0);
        System.out.println(byDateId);


    }



}