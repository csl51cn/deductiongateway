package org.starlightfinancial.deductiongateway.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/7/15 13:10
 * @Modified By:
 */
@Service
public class WhiteListStrategyContext {

    @Autowired
    private final Map<String, WhiteListStrategy> STRATEGY_MAP = new ConcurrentHashMap<>();


    public Map<String, String> createWhiteList(List<AccountManager> list, String type) {
        return STRATEGY_MAP.get(type).createWhiteList(list);
    }
}
