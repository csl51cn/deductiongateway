package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 卡号管理Service实现类
 */
@Service
public class AccountManagerServiceImpl implements AccountManagerService {
    @Autowired
    private AccountManagerRepository accountManagerRepository;


    /**
     * 查询卡号
     *
     * @param contractNo  合同编号
     * @param bizNo       业务编号
     * @param accountName 账户名
     * @param pageBean    分页对象
     * @return 返回分页对象
     */
    @Override
    public PageBean queryAccount(String contractNo, String bizNo, String accountName, PageBean pageBean) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);

        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //不为空则加条件
                if (StringUtils.isNotEmpty(contractNo)) {
                    predicates.add(cb.equal(root.get("contractNo"), contractNo));
                }
                if (StringUtils.isNotEmpty(bizNo)) {
                    predicates.add(cb.equal(root.get("bizNo"), bizNo));
                }
                if (StringUtils.isNotEmpty(accountName)) {
                    predicates.add(cb.equal(root.get("accountName"), accountName));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };

        Page<AccountManager> accountManagers = accountManagerRepository.findAll(specification, pageRequest);
        if (accountManagers.hasContent()) {
            pageBean.setTotal(accountManagers.getTotalElements());
            pageBean.setRows(accountManagers.getContent());
            return pageBean;
        }
        return null;
    }


    /**
     * 更新操作
     *
     * @param accountManager
     */
    @Override
    public void updateAccount(AccountManager accountManager) {
        accountManager.setChangeTime(new Date());
        accountManagerRepository.saveAndFlush(accountManager);
    }

    /**
     * 查询最后一条记录
     * @return
     */
    @Override
    public List findLastAccount() {
        PageBean pageBean = new PageBean();
        pageBean.setPageSize(1);
        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();

                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<AccountManager> accountManagers = accountManagerRepository.findAll(specification, pageRequest);
        return accountManagers.getContent();
    }


}
