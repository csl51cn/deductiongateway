package org.starlightfinancial.deductiongateway.service.impl;


import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;

/**
 * @author: senlin.deng
 * @Description: 账号管理批量导入处理
 * @date: Created in 2018/5/3 9:42
 * @Modified By:
 */
public class AccountManagerItemProcessor implements ItemProcessor<AccountManager, AccountManager> {

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Override
    public AccountManager process(AccountManager item) throws Exception {
        //在写入数据库之前,用date_id和银行卡号查询记录是否已经存在,如果已经存在就不保存,如果不存在就保存记录
        AccountManager existAccountManager = accountManagerRepository.findByDateIdAndAccountAndAccountNameAndCertificateNo(item.getDateId(), item.getAccount(), item.getAccountName(), item.getCertificateNo());
        if (existAccountManager == null) {
            return item;
        } else {
            return null;
        }
    }
}
