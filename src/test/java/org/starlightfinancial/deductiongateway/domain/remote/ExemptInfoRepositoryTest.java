package org.starlightfinancial.deductiongateway.domain.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/20 17:07
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ExemptInfoRepositoryTest {

    @Autowired
    ExemptInfoRepository exemptInfoRepository;


    @Test
    public void findOne() {
        ExemptInfo one = exemptInfoRepository.findOne(1);
        System.out.println(one);
    }

    @Test
    public void save() {
        List<ExemptInfo> save = exemptInfoRepository.save(new ArrayList<>());
        System.out.println(save);
    }
}