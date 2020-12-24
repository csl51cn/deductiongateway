package org.starlightfinancial.deductiongateway.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.domain.remote.DataWorkInfo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/12/24 14:50
 * @Modified By:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DataWorkInfoDaoTest {
    @Autowired
    private DataWorkInfoDao dataWorkInfoDao;

    @Test
    public void findServiceChargeCompanyByContractNoIn() {
        ArrayList<String> contractNos = new ArrayList<>();
        contractNos.add("JK991700823");
        contractNos.add("JK991700830");
        contractNos.add("JK991800006");
        contractNos.add("JK991700826");
        contractNos.add("JK991700834");
        contractNos.add("JK991700831");
        List<DataWorkInfo> serviceChargeCompanyByContractNoIn = dataWorkInfoDao.findServiceChargeCompanyByContractNoIn(contractNos);
        System.out.println(serviceChargeCompanyByContractNoIn);
    }

}
