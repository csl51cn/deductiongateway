package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.LimitManager;
import org.starlightfinancial.deductiongateway.domain.local.LimitManagerRepository;
import org.starlightfinancial.deductiongateway.enums.ConstantsEnum;
import org.starlightfinancial.deductiongateway.service.LimitManagerService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.ServletUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by senlin.deng on 2017-08-31.
 */
@Service
public class LimitManagerServiceImpl implements LimitManagerService {

    private static final Logger log = LoggerFactory.getLogger(LimitManagerServiceImpl.class);
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
     * 保存限额
     *
     * @param limitManager
     */
    @Override
    public void saveLimit(LimitManager limitManager) {
        limitManagerRepository.saveAndFlush(limitManager);
        log.info("新增限额信息,操作人:[{}],限额记录id:[{}]", Utility.getLoginUserName(ServletUtil.getSession()),
                limitManager.getId());
    }

    /**
     * 更新限额
     *
     * @param limitManager
     */
    @Override
    public void updateLimit(LimitManager limitManager) {
        LimitManager limitManagerOriginal = limitManagerRepository.findOne(limitManager.getId());
        String differenceFieldValue = Utility.compareObjectFieldValue(limitManagerOriginal, limitManager);
        log.info("更新限额信息,操作人:[{}],限额记录id:[{}],更新情况:{}", Utility.getLoginUserName(ServletUtil.getSession()),
                limitManager.getId(), differenceFieldValue);
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

    /**
     * 修改启用状态
     *
     * @param ids     一个或多个记录的id
     */
    @Override
    public void modifyEnabledStatus(String ids) {
        //处理ids,先用逗号切割,然后转为List
        List<Integer> idsList = Arrays.stream(ids.split(",")).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        //根据id查询对应的记录
        List<LimitManager> all = limitManagerRepository.findAll(idsList);
        //设置成启用的id
        ArrayList<Integer> setEnabled = new ArrayList<>();
        //设置成停用id
        ArrayList<Integer> setDisabled = new ArrayList<>();
        all.forEach(limitManager -> {
            if (StringUtils.equals(limitManager.getEnabled(), ConstantsEnum.SUCCESS.getCode())) {
                limitManager.setEnabled(ConstantsEnum.FAIL.getCode());
                setDisabled.add(limitManager.getId());
            } else {
                limitManager.setEnabled(ConstantsEnum.SUCCESS.getCode());
                setEnabled.add(limitManager.getId());
            }

            limitManagerRepository.saveAndFlush(limitManager);
        });

        log.info("修改限额启用状态成功,操作人:[{}],修改为启用记录id:{},修改为禁用记录id:{}"
                , Utility.getLoginUserName(ServletUtil.getSession()), setEnabled, setDisabled);
    }




}
