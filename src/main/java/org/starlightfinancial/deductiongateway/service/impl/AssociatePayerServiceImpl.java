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
import org.starlightfinancial.deductiongateway.domain.local.AssociatePayer;
import org.starlightfinancial.deductiongateway.domain.local.AssociatePayerRepository;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.service.AssociatePayerService;
import org.starlightfinancial.deductiongateway.service.CacheService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 关联还款人管理Service接口实现类
 * @date: Created in 2018/7/23 11:46
 * @Modified By:
 */
@Service
public class AssociatePayerServiceImpl implements AssociatePayerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssociatePayerServiceImpl.class);
    @Autowired
    private AssociatePayerRepository associatePayerRepository;

    /**
     * 查找所有记录
     *
     * @return 关联还款人
     */
    @Override
    public List<AssociatePayer> findAll() {
        return associatePayerRepository.findAll();
    }

    /**
     * 根据条件查询记录
     *
     * @param pageBean   页面参数对象
     * @param contractNo 合同号
     * @return 查询出来的记录
     */
    @Override
    public PageBean queryAssociatePayer(PageBean pageBean, String contractNo) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 0);
        Page<AssociatePayer> associatePayers = associatePayerRepository.findAll(new Specification<AssociatePayer>() {
            @Override
            public Predicate toPredicate(Root<AssociatePayer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                //合同号非空判断。不为空则加此条件
                if (StringUtils.isNotBlank(contractNo)) {
                    predicates.add(cb.equal(root.get("contractNo"), contractNo));
                }
                return cb.and(predicates.toArray(new Predicate[]{}));
            }
        }, pageRequest);
        if (associatePayers.hasContent()) {
            pageBean.setTotal(associatePayers.getTotalElements());
            pageBean.setRows(associatePayers.getContent());
            return pageBean;
        }
        return null;
    }

    /**
     * 保存记录
     *
     * @param associatePayer 关联还款人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveAssociatePayer(AssociatePayer associatePayer) {
        Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();
        BusinessTransaction businessTransaction = businessTransactionCacheMap.get(associatePayer.getContractNo());
        if (businessTransaction != null) {
            associatePayer.setDateId(businessTransaction.getDateId());
        }
        associatePayerRepository.saveAndFlush(associatePayer);
        LOGGER.info("新增非代扣还款信息操作,操作人:{},非代扣还款记录id:{}", associatePayer.getCreateId(), associatePayer.getId());
    }

    /**
     * 更新记录
     *
     * @param associatePayer 关联还款人
     */
    @Override
    public void updateAssociatePayer(AssociatePayer associatePayer) {
        if (associatePayer.getDateId() == null || associatePayer.getDateId().intValue() == 0) {
            Map<String, BusinessTransaction> businessTransactionCacheMap = CacheService.getBusinessTransactionCacheMap();
            BusinessTransaction businessTransaction = businessTransactionCacheMap.get(associatePayer.getContractNo());
            if (businessTransaction != null) {
                associatePayer.setDateId(businessTransaction.getDateId());
            }
        }
        associatePayerRepository.saveAndFlush(associatePayer);
        LOGGER.info("更新非代扣还款信息操作,操作人:{},非代扣还款记录id:{}", associatePayer.getCreateId(), associatePayer.getId());
    }


}
