package org.starlightfinancial.deductiongateway.domain.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/24 10:27
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FailEntryAccountRepositoryTest {

    @Autowired
    private  FailEntryAccountRepository  failEntryAccountRepository;

    @Test
    public  void  save(){
        FailEntryAccount failEntryAccount = new FailEntryAccount();
        failEntryAccount.setContractNo("123");
        failEntryAccount.setGmtCreate(new Date());
        failEntryAccountRepository.save(failEntryAccount);

    }

}