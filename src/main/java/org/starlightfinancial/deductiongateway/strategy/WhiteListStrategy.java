package org.starlightfinancial.deductiongateway.strategy;

import org.starlightfinancial.deductiongateway.domain.local.AccountManager;

import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/7/15 13:07
 * @Modified By:
 */
public interface WhiteListStrategy {

    /**
     * 生成白名单
     * @param list 卡号信息
     * @return 生成白名单信息
     */
    Map<String, String> createWhiteList(List<AccountManager> list);
}
