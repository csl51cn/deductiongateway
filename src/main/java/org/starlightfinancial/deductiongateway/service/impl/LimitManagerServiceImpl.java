package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by senlin.deng on 2017-08-31.
 */
@Service
public class LimitManagerServiceImpl implements LimitManagerService {

    @Autowired
    private LimitManagerRepository limitManagerRepository;

    /**
     * 查询所有数据
     *
     * @param pageBean
     * @param limitManager
     * @return
     */
    @Override
    public PageBean queryLimit(PageBean pageBean, LimitManager limitManager) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);
        Page<LimitManager> limitManagers = limitManagerRepository.findAll(new Specification<LimitManager>() {
            @Override
            public Predicate toPredicate(Root<LimitManager> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //如果银行名称不为空,添加到查询条件
                if (StringUtils.isNotBlank(limitManager.getBankName())) {
                    predicates.add(cb.equal(root.get("bankName"), limitManager.getBankName()));
                }
                //如果代扣渠道不为空,添加到查询条件
                if (StringUtils.isNotBlank(limitManager.getChannel())) {
                    predicates.add(cb.equal(root.get("channel"), limitManager.getChannel()));
                }
                //如果启用状态不为空,添加到查询条件
                if (StringUtils.isNotBlank(limitManager.getEnabled())) {
                    predicates.add(cb.equal(root.get("enabled"), limitManager.getEnabled()));
                }

                return cb.and(predicates.toArray(new Predicate[]{}));
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
     *
     * @param limitManager
     */
    @Override
    public void saveOrUpdateLimit(LimitManager limitManager) {
        limitManagerRepository.saveAndFlush(limitManager);
    }

    /**
     * 通过银行编码和渠道查询是否存在记录
     *
     * @param bankCode 银行编码
     * @param channel  渠道
     * @return 结果
     */
    @Override
    public boolean isExisted(String bankCode, String channel) {
        LimitManager byBankCodeAndChannel = limitManagerRepository.findByBankCodeAndChannel(bankCode, channel);
        return byBankCodeAndChannel != null;
    }


}
