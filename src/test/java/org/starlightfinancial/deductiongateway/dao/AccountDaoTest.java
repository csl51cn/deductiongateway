package org.starlightfinancial.deductiongateway.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;

import javax.transaction.Transactional;
import java.util.List;

/**
 * @author: senlin.deng
 * @Description:
 * @date: Created in 2018/5/7 14:34
 * @Modified By:
 */

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class AccountDaoTest {
    @Autowired
    private AccountDao accountDao;


    @Test
    public void findDateIdByBizNo() {
        Integer dateIdByBizNo = accountDao.findDateIdByBizNo("56000001");
        System.out.println(dateIdByBizNo);
    }

    @Test
    public void findAccountByDateId() {
        List<AccountManager> accountManagerList = accountDao.findAccountByDateId(765477);
        for(AccountManager accountManager:accountManagerList){
            System.out.println(accountManager);
        }
    }

}