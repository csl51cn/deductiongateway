package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.ExtraProcessing;
import org.starlightfinancial.deductiongateway.domain.local.ExtraProcessingRepository;
import org.starlightfinancial.deductiongateway.service.ExtraProcessingService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * 额外处理Service接口实现类
 */
@Service
public class ExtraProcessingServiceImpl implements ExtraProcessingService {

    @Autowired
    private ExtraProcessingRepository extraProcessingRepository;

    /**
     * 查询所有额外处理记录
     * @param pageBean
     * @return
     */
    @Override
    public PageBean queryAllLimit(PageBean pageBean) {
        PageRequest pageRequest = Utility.buildPageRequest(pageBean);
        Page<ExtraProcessing> extraProcessings = extraProcessingRepository.findAll(new Specification<ExtraProcessing>() {
            @Override
            public Predicate toPredicate(Root<ExtraProcessing> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return null;
            }
        }, pageRequest);
        if (extraProcessings.hasContent()) {
            pageBean.setTotal(extraProcessings.getTotalElements());
            pageBean.setRows(extraProcessings.getContent());
            return pageBean;
        }
        return null;
    }

    @Override
    public void updateProcessing(ExtraProcessing extraProcessing) {
        extraProcessingRepository.saveAndFlush(extraProcessing);
    }

    @Override
    public List<ExtraProcessing> findProcessingByIds(String ids) {
        String[] idsStrArr = ids.split(",");
        ArrayList<Integer> idsList = new ArrayList<>();
        for (String id : idsStrArr) {
            idsList.add(Integer.parseInt(id));
        }
        List<ExtraProcessing> processings = extraProcessingRepository.findByIdIn(idsList.toArray(new Integer[]{}));
        return processings;
    }
}
