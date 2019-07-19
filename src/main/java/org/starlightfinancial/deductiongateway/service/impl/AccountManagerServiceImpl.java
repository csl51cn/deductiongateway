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
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.dao.AccountDao;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.AccountManagerRepository;
import org.starlightfinancial.deductiongateway.service.AccountManagerService;
import org.starlightfinancial.deductiongateway.strategy.WhiteListStrategyContext;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;
import org.starlightfinancial.deductiongateway.vo.AccountManagerVO;

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
    private WhiteListStrategyContext whiteListStrategyContext;


    /**
     * 查询卡号信息
     *
     * @param accountManagerVO 查询条件
     * @param pageBean         分页对象
     * @return 返回分页对象
     */
    @Override
    public PageBean queryAccount(AccountManagerVO accountManagerVO, PageBean pageBean) {

        Specification<AccountManager> specification = getAccountManagerSpecification(accountManagerVO);
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

    private Specification<AccountManager> getAccountManagerSpecification(AccountManagerVO accountManagerVO) {
        return new Specification<AccountManager>() {
            @Override
            public Predicate toPredicate(Root<AccountManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //不为空则加条件
                if (StringUtils.isNotBlank(accountManagerVO.getContractNo())) {
                    predicates.add(cb.equal(root.get("contractNo"), accountManagerVO.getContractNo()));
                }
                if (StringUtils.isNotBlank(accountManagerVO.getBizNo())) {
                    predicates.add(cb.equal(root.get("bizNo"), accountManagerVO.getBizNo()));
                }
                if (StringUtils.isNotBlank(accountManagerVO.getAccountName())) {
                    predicates.add(cb.equal(root.get("accountName"), accountManagerVO.getAccountName()));
                }

                if (Objects.nonNull(accountManagerVO.getLoanStartDate())) {
                    predicates.add(cb.greaterThanOrEqualTo(root.get("loanDate"), accountManagerVO.getLoanStartDate()));
                }
                if (Objects.nonNull(accountManagerVO.getLoanEndDate())) {
                    predicates.add(cb.lessThanOrEqualTo(root.get("loanDate"), accountManagerVO.getLoanEndDate()));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        };
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
    public AccountManager findLastAccount() {

        return accountManagerRepository.findTopByOrderByIdDesc();
    }

    /**
     * 添加代扣卡信息
     *
     * @param bizNo 业务编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Message addAccount(String bizNo) {

        //根据业务编号查询已经保存了的卡号信息
        List<AccountManager> existedAccountManagerList = accountManagerRepository.findByBizNo(bizNo);
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

    /**
     * 根据id查询卡信息
     *
     * @param id
     * @return
     */
    @Override
    public AccountManager findById(Integer id) {
        return accountManagerRepository.findOne(id);
    }

    /**
     * 按条件查询卡号记录,并生成白明单,map是文件名与白名单内容的映射
     *
     * @param accountManagerVO
     * @return
     */
    @Override
    public Map<String, String> whiteListExport(AccountManagerVO accountManagerVO) {
        Specification<AccountManager> specification = getAccountManagerSpecification(accountManagerVO);
        List<AccountManager> all = accountManagerRepository.findAll(specification);
        HashMap<String, String> map = new HashMap<>(4);
        if (Objects.isNull(all) || all.size() == 0) {
            return map;
        }
        map.putAll(whiteListStrategyContext.createWhiteList(all, "chinaPayClearNet"));
        map.putAll(whiteListStrategyContext.createWhiteList(all, "unionPay"));
        return map;
    }

}
