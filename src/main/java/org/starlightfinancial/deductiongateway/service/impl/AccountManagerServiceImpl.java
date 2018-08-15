package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.ChinaPayConfig;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.dao.AccountDao;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 卡号管理Service实现类
 *
 * @author senlin.deng
 */
@Service
public class AccountManagerServiceImpl implements AccountManagerService {

    private static final Logger logger = LoggerFactory.getLogger(AccountManagerServiceImpl.class);

    @Autowired
    private AccountManagerRepository accountManagerRepository;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private ChinaPayConfig chinaPayConfig;
    @Autowired
    private BaofuConfig baofuConfig;

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
        long count = accountManagerRepository.count(specification);
        if (count == 0) {
            //如果查询出来的总记录数为0,直接返回null,避免后续查询代码执行
            return null;
        }
        double tempTotalPageCount = count / (pageBean.getPageSize().doubleValue());
        double totalPageCount = Math.ceil(tempTotalPageCount == 0 ? 1 : tempTotalPageCount);
        if (totalPageCount < pageBean.getPageNumber()) {
            pageBean.setPageNumber(1);
        }
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);
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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAccount(AccountManager accountManager) {
        accountManager.setChangeTime(new Date());
        accountManagerRepository.saveAndFlush(accountManager);
    }

    /**
     * 查询最后一条记录
     *
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

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message addAccount(String bizNo) {
        Specification<AccountManager> specification = new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (StringUtils.isNotEmpty(bizNo)) {
                    predicates.add(cb.equal(root.get("bizNo"), bizNo));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
        //根据业务编号查询已经保存了的卡号信息
        List<AccountManager> existedAccountManagerList = accountManagerRepository.findAll(specification);
        //查询date_id
        Integer dateId = accountDao.findDateIdByBizNo(bizNo);
        //根据date_id查询业务系统代扣卡信息
        List<AccountManager> accountManagerList = accountDao.findAccountByDateId(dateId);
        //根据sort升序排序
        accountManagerList.sort(Comparator.comparingInt(AccountManager::getSort));
        if (accountManagerList.size() == 0) {
            return Message.fail("添加失败,未查询到代扣卡");
        }

        Iterator<AccountManager> iterator = accountManagerList.iterator();


        while (iterator.hasNext()) {
            AccountManager accountManager = iterator.next();
            if (existedAccountManagerList.size() > 0) {
                //去重,避免重复入库
                for (AccountManager existedAccountManager : existedAccountManagerList) {
                    //通过date_id,账户名,银行卡号,身份证,手机号对比,如果这五个字段相同,那么就认为两个对象相同
                    boolean isRepeated = existedAccountManager.getDateId().equals(accountManager.getDateId()) && StringUtils.equals(existedAccountManager.getAccountName(), accountManager.getAccountName()) &&
                            StringUtils.equals(existedAccountManager.getAccount(), accountManager.getAccount()) && StringUtils.equals(existedAccountManager.getCertificateNo(), accountManager.getCertificateNo()) &&
                            StringUtils.equals(existedAccountManager.getMobile(), accountManager.getMobile());
                    //如果数据库中已经有当前记录,从待保存的List中删除,避免重复入库
                    if (isRepeated) {
                        iterator.remove();
                        break;
                    }
                }

            }
        }

        accountManagerRepository.save(accountManagerList);
        return Message.success("添加代扣卡成功");

    }

}
