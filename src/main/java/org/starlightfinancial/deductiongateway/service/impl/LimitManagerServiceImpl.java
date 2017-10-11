package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.domain.local.LimitManagerRepository;
import org.starlightfinancial.deductiongateway.service.LimitManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * Created by senlin.deng on 2017-08-31.
 */
@Service
public class LimitManagerServiceImpl implements LimitManagerService {

    @Autowired
    private LimitManagerRepository limitManagerRepository;

    /**
     * 查询所有数据
     * @param pageBean
     * @return
     */
    @Override
    public PageBean queryAllLimit(PageBean pageBean) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean,1);
        Page<LimitManager> limitManagers = limitManagerRepository.findAll(new Specification<LimitManager>() {
            @Override
            public Predicate toPredicate(Root<LimitManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        }, pageRequest);
        if (limitManagers.hasContent()) {
            pageBean.setTotal(limitManagers.getTotalElements());
            pageBean.setRows(limitManagers.getContent());
            return pageBean;
        }
        return null;
    }

    /**
     * 保存或更新限额
     * @param limitManager
     */
    @Override
    public void saveOrUpdateLimit(LimitManager limitManager) {
        limitManagerRepository.saveAndFlush(limitManager);
    }


}
