package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssue;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfoRepository;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueRepository;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/9/18 16:33
 * @Modified By:
 */
@Service
public class LoanIssueServiceImpl implements LoanIssueService {

    @Autowired
    private LoanIssueBasicInfoRepository loanIssueBasicInfoRepository;

    @Autowired
    private LoanIssueRepository loanIssueRepository;

    @Autowired
    private BaofuConfig baofuConfig;


    /**
     * 通过接收前端传入的贷款发放基本信息进行放款操作
     *
     * @param loanIssueBasicInfos 贷款发放基本信息
     */
    @Override
    public void loanIssue(List<LoanIssueBasicInfo> loanIssueBasicInfos) {
        ArrayList<String> transactionNos = new ArrayList<>();
        //生成商户订单号:商户编号+14位日期+3位随机数
        int[] randomArray = Utility.randomArray(0, 999, loanIssueBasicInfos.size());
        Arrays.stream(randomArray).forEach(i -> {
            StringBuilder transactionNo = new StringBuilder(baofuConfig.getClassicPayMemberId());
            transactionNo.append(Utility.getTimestamp()).append(String.format("%3d", i));
            transactionNos.add(transactionNo.toString());
        });


        //数据入库
        for (int i = 0; i<loanIssueBasicInfos.size();i++){
            LoanIssueBasicInfo loanIssueBasicInfo = loanIssueBasicInfos.get(i);
            loanIssueBasicInfo.setGmtCreate(new Date());
            loanIssueBasicInfo.setGmtModified(loanIssueBasicInfo.getGmtCreate());
            loanIssueBasicInfo.setModifiedId(loanIssueBasicInfo.getCreateId());

            LoanIssue loanIssue = new LoanIssue();
            //设置订单号
            loanIssue.setTransactionNo(transactionNos.get(i));
            loanIssue.setGmtCreate(new Date());
            loanIssue.setCreateId(loanIssueBasicInfo.getCreateId());


        }





    }
}
