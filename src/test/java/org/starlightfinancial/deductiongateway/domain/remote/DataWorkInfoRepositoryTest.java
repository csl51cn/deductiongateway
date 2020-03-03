package org.starlightfinancial.deductiongateway.domain.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/3/3 13:44
 * @Modified By:
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DataWorkInfoRepositoryTest {


    @Autowired
    DataWorkInfoRepository  dataWorkInfoRepository;


    @Test
    public   void  findOne(){
        DataWorkInfo one = dataWorkInfoRepository.findOne(58636);

        System.out.println(one);
    }
}