package org.starlightfinancial.deductiongateway.domain.local;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/18 13:14
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LoanIssueBasicInfoTest {
    @Autowired
    private  LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Autowired
    private LoanIssueRepository loanIssueRepository;

    @Test
    public void  test1(){
        ArrayList<LoanIssueBasicInfo> loanIssueBasicInfos = new ArrayList<>();

        LoanIssueBasicInfo loanIssueBasicInfo = new LoanIssueBasicInfo();

        loanIssueBasicInfo.setBusinessNo("123");
        loanIssueBasicInfo.setContractNo("JK123");
        loanIssueBasicInfo.setDateId(123L);
        loanIssueBasicInfo.setToAccountType("1");
        loanIssueBasicInfo.setIssueAmount(new BigDecimal(123));
        loanIssueBasicInfo.setToAccountName("张三");
        loanIssueBasicInfo.setToBankNameId(520);
        loanIssueBasicInfo.setToBankBranch("重庆渝中支行");
        loanIssueBasicInfo.setIdentityNo("i1234");
        loanIssueBasicInfo.setMobileNo("p1234");
        loanIssueBasicInfo.setChannel("宝付");
        loanIssueBasicInfo.setCreateId(14);
        loanIssueBasicInfo.setModifiedId(loanIssueBasicInfo.getCreateId());
        loanIssueBasicInfo.setGmtCreate(new Date());
        loanIssueBasicInfo.setGmtModified(loanIssueBasicInfo.getGmtCreate());

        loanIssueBasicInfos.add(loanIssueBasicInfo);

        LoanIssueBasicInfo loanIssueBasicInfo1 = new LoanIssueBasicInfo();

        loanIssueBasicInfo1.setBusinessNo("456");
        loanIssueBasicInfo1.setContractNo("JK456");
        loanIssueBasicInfo1.setDateId(456L);
        loanIssueBasicInfo1.setToAccountType("1");
        loanIssueBasicInfo1.setIssueAmount(new BigDecimal(456));
        loanIssueBasicInfo1.setToAccountName("李四");
        loanIssueBasicInfo1.setToBankNameId(520);
        loanIssueBasicInfo1.setToBankBranch("重庆江北支行");
        loanIssueBasicInfo1.setIdentityNo("i456");
        loanIssueBasicInfo1.setMobileNo("p456");
        loanIssueBasicInfo1.setChannel("宝付");
        loanIssueBasicInfo1.setCreateId(14);
        loanIssueBasicInfo1.setModifiedId(loanIssueBasicInfo.getCreateId());
        loanIssueBasicInfo1.setGmtCreate(new Date());
        loanIssueBasicInfo1.setGmtModified(loanIssueBasicInfo.getGmtCreate());
        loanIssueBasicInfos.add(loanIssueBasicInfo1);



        System.out.println(JSON.toJSONString(loanIssueBasicInfos));

        LoanIssue loanIssue = new LoanIssue();
        loanIssue.setTransactionNo("b123123");
        loanIssue.setTransactionSummary("kadfjk");
        loanIssue.setLoanIssueBasicInfo(loanIssueBasicInfo);
        loanIssue.setCreateId(14);
        loanIssue.setModifiedId(loanIssue.getCreateId());
        loanIssue.setGmtCreate(new Date());
        loanIssue.setGmtModified(loanIssue.getGmtCreate());
        loanIssue.setTransactionStartTime(new Date());
        loanIssueBasicInfo.setLoanIssues(Collections.singletonList(loanIssue));
//        loanIssueBasicInfoRepository.save(loanIssueBasicInfo);


    }


    @Test
    public void  test2(){
        ArrayList<LoanIssueBasicInfo> loanIssueBasicInfos = new ArrayList<>();

        LoanIssueBasicInfo loanIssueBasicInfo = new LoanIssueBasicInfo();

        loanIssueBasicInfo.setBusinessNo("9900847");
        loanIssueBasicInfo.setContractNo("JK9900847");
        loanIssueBasicInfo.setDateId(990L);
        loanIssueBasicInfo.setToAccountType("1");
        loanIssueBasicInfo.setIssueAmount(new BigDecimal(10));
        loanIssueBasicInfo.setToAccountName("测试账号");
        loanIssueBasicInfo.setToAccountNo("666666666");
        loanIssueBasicInfo.setToBankNameId(518);
        loanIssueBasicInfo.setToBankBranch("支行");
        loanIssueBasicInfo.setIdentityNo("320301198502169142");
        loanIssueBasicInfo.setMobileNo("15831783630");
        loanIssueBasicInfo.setChannel("宝付");
        loanIssueBasicInfo.setCreateId(14);
        loanIssueBasicInfo.setModifiedId(loanIssueBasicInfo.getCreateId());
        loanIssueBasicInfo.setGmtCreate(new Date());
        loanIssueBasicInfo.setGmtModified(loanIssueBasicInfo.getGmtCreate());

        loanIssueBasicInfos.add(loanIssueBasicInfo);

        LoanIssueBasicInfo loanIssueBasicInfo1 = new LoanIssueBasicInfo();

        loanIssueBasicInfo1.setBusinessNo("9900598");
        loanIssueBasicInfo1.setContractNo("JK9900598");
        loanIssueBasicInfo1.setDateId(589L);
        loanIssueBasicInfo1.setToAccountType("1");
        loanIssueBasicInfo1.setToAccountNo("666666666");
        loanIssueBasicInfo1.setIssueAmount(new BigDecimal(99990));
        loanIssueBasicInfo1.setToAccountName("测试账号");
        loanIssueBasicInfo1.setToBankNameId(518);
        loanIssueBasicInfo1.setToBankBranch("支行");
        loanIssueBasicInfo1.setIdentityNo("320301198502169142");
        loanIssueBasicInfo1.setMobileNo("15831783630");
        loanIssueBasicInfo1.setChannel("宝付");
        loanIssueBasicInfo1.setCreateId(14);
        loanIssueBasicInfo1.setModifiedId(loanIssueBasicInfo.getCreateId());
        loanIssueBasicInfo1.setGmtCreate(new Date());
        loanIssueBasicInfo1.setGmtModified(loanIssueBasicInfo.getGmtCreate());
        loanIssueBasicInfos.add(loanIssueBasicInfo1);



        System.out.println(JSON.toJSONString(loanIssueBasicInfos));




    }

}