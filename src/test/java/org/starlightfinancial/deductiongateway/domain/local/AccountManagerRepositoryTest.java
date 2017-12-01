package org.starlightfinancial.deductiongateway.domain.local;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by senlin.deng on 2017-10-11.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@SpringBootTest
public class AccountManagerRepositoryTest {

    @Autowired
    private AccountManagerRepository accountManagerRepository;


    @Autowired
    private AccountManagerService accountManagerService;


    @Test
    public void function1() throws Exception {
//        List<AccountManager> all = accountManagerRepository.findAll();
//        for (AccountManager ac :
//                all) {
//            System.out.println(ac);
//        }
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
        if (accountManagers.hasContent()) {
            pageBean.setTotal(accountManagers.getTotalElements());
            pageBean.setRows(accountManagers.getContent());
        }
        System.out.println(pageBean);
    }

    @Test
    public void function2()throws Exception {
        AccountManager ac = accountManagerRepository.findByAccountAndSortAndContractNo("5240943764011888", 1, "JK991601111");
        System.out.println(ac);
    }




}