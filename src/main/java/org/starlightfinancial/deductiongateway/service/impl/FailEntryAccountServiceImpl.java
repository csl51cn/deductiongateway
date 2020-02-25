package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.domain.local.FailEntryAccount;
import org.starlightfinancial.deductiongateway.domain.local.FailEntryAccountRepository;
import org.starlightfinancial.deductiongateway.service.FailEntryAccountService;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.Date;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/23 13:02
 * @Modified By:
 */
@Service
public class FailEntryAccountServiceImpl implements FailEntryAccountService {
    @Autowired
    private FailEntryAccountRepository failEntryAccountRepository;


    /**
     * 保存不能足额入账的信息
     *
     * @param failEntryAccount
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(FailEntryAccount failEntryAccount) {
        failEntryAccountRepository.save(failEntryAccount);
    }

    /**
     * 通过条件查询记录
     *
     * @param contractNo 合同号
     * @param date       生成日期
     * @return
     */
    @Override
    public FailEntryAccount findByContractNoAndCreateDate(String contractNo, Date date) {
        date = Utility.convertToDate(Utility.convertToString(date, "yyyy-MM-dd"), "yyyy-MM-dd");
        return failEntryAccountRepository.findByContractNoAndGmtCreateGreaterThanEqual(contractNo, date);

    }
}
