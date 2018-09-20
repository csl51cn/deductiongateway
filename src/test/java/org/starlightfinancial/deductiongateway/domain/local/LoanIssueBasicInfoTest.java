package org.starlightfinancial.deductiongateway.domain.local;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
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
        LoanIssueBasicInfo loanIssueBasicInfo = new LoanIssueBasicInfo();

        loanIssueBasicInfo.setBusinessNo("123");
        loanIssueBasicInfo.setContractNo("JK123");
        loanIssueBasicInfo.setDateId(123L);
        loanIssueBasicInfo.setToAccountType("1");
        loanIssueBasicInfo.setIssueAmount(new BigDecimal(123));
        loanIssueBasicInfo.setToAccountName("张三");
        loanIssueBasicInfo.setToBankName("中国银行");
        loanIssueBasicInfo.setToBankBranch("重庆渝中支行");
        loanIssueBasicInfo.setIdentityNo("i1234");
        loanIssueBasicInfo.setMobileNo("p1234");
        loanIssueBasicInfo.setChannel("宝付");
        loanIssueBasicInfo.setIsIssue("0");
        loanIssueBasicInfo.setCreateId(14);
        loanIssueBasicInfo.setModifiedId(loanIssueBasicInfo.getCreateId());
        loanIssueBasicInfo.setGmtCreate(new Date());
        loanIssueBasicInfo.setGmtModified(loanIssueBasicInfo.getGmtCreate());

        System.out.println(JSON.toJSONString(loanIssueBasicInfo));

        LoanIssue loanIssue = new LoanIssue();
        loanIssue.setTransactionNo("b123123");
        loanIssue.setTransactionSummary("kadfjk");

        loanIssue.setCreateId(14);
        loanIssue.setModifiedId(loanIssue.getCreateId());
        loanIssue.setGmtCreate(new Date());
        loanIssue.setGmtModified(loanIssue.getGmtCreate());
        loanIssue.setTransactionStartTime(new Date());
        loanIssueBasicInfo.setLoanIssue(loanIssue);
//        loanIssueBasicInfoRepository.save(loanIssueBasicInfo);


    }

}