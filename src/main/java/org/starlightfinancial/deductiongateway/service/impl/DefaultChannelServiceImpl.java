package org.starlightfinancial.deductiongateway.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.DefaultChannel;
import org.starlightfinancial.deductiongateway.domain.local.DefaultChannelRepository;
import org.starlightfinancial.deductiongateway.service.DefaultChannelService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.ServletUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: Senlin.Deng
 * @Description: 默认渠道管理Service实现
 * @date: Created in 2019/7/10 10:42
 * @Modified By:
 */
@Service
@Slf4j
public class DefaultChannelServiceImpl implements DefaultChannelService {

    @Autowired
    private DefaultChannelRepository defaultChannelRepository;

    /**
     * 通过银行编码查询记录是否存在
     *
     * @param bankCode 银行编码
     * @return
     */
    @Override
    public boolean isExisted(String bankCode) {
        return Objects.nonNull(getByBankCode(bankCode));
    }

    /**
     * 保存记录
     *
     * @param defaultChannel 默认渠道
     * @return 影响的行数
     */
    @Override
    public void saveDefaultChannel(DefaultChannel defaultChannel) {
        defaultChannelRepository.saveAndFlush(defaultChannel);
        log.info("新增默认渠道信息,操作人:[{}], 默认渠道记录id:[{}]", Utility.getLoginUserName(ServletUtil.getSession()),
                defaultChannel.getId());
    }

    /**
     * 条件查询记录
     *
     * @param pageBean       分页参数
     * @param defaultChannel 包含查询条件
     * @return
     */
    @Override
    public PageBean queryDefaultChannel(PageBean pageBean, DefaultChannel defaultChannel) {

        PageRequest pageRequest = Utility.buildPageRequest(pageBean, 1);
        Page<DefaultChannel> defaultChannels = defaultChannelRepository.findAll((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            //如果银行名称不为空,添加到查询条件
            if (StringUtils.isNotBlank(defaultChannel.getBankName())) {
                predicates.add(cb.equal(root.get("bankName"), defaultChannel.getBankName()));
            }
            //如果代扣渠道不为空,添加到查询条件
            if (StringUtils.isNotBlank(defaultChannel.getDefaultChannel())) {
                predicates.add(cb.equal(root.get("defaultChannel"), defaultChannel.getDefaultChannel()));
            }
            return cb.and(predicates.toArray(new Predicate[]{}));
        }, pageRequest);
        if (!defaultChannels.hasContent()) {
            return null;
        }
        pageBean.setTotal(defaultChannels.getTotalElements());
        pageBean.setRows(defaultChannels.getContent());
        return pageBean;
    }

    /**
     * 更新记录
     *
     * @param defaultChannel
     */
    @Override
    public void updateDefaultChannel(DefaultChannel defaultChannel) {
        DefaultChannel defaultChannelOriginal = defaultChannelRepository.findOne(defaultChannel.getId());
        String differenceFieldValue = Utility.compareObjectFieldValue(defaultChannelOriginal, defaultChannel);
        log.info("更新限额信息,操作人:[{}],限额记录id:[{}],更新情况:{}", Utility.getLoginUserName(ServletUtil.getSession()),
                defaultChannel.getId(), differenceFieldValue);
        defaultChannelRepository.saveAndFlush(defaultChannel);
    }

    /**
     * 通过银行编码查询记录
     *
     * @param bankCode
     * @return
     */
    @Override
    public DefaultChannel getByBankCode(String bankCode) {
        return defaultChannelRepository.getByBankCode(bankCode);

    }

}
