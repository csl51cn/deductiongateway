package org.starlightfinancial.deductiongateway.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/23 10:32
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Rollback
public class CacheServiceTest {

    @Test
    public void getBusinessTransactionCacheMap() {
        Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getInstance().getBusinessTransactionCacheMap();
        businessTransactionCacheMap.forEach((k,v)->{
            System.out.println(k +"----"+v);

        });
    }
}