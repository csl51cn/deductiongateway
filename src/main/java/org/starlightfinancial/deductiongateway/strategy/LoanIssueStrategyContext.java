package org.starlightfinancial.deductiongateway.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.enums.LoanIssueChannelEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放策略上下文
 * @date: Created in 2018/9/20 14:46
 * @Modified By:
 */
@Component
public class LoanIssueStrategyContext {
    @Autowired
    private final Map<String, LoanIssueStrategy> STRATEGY_MAP = new ConcurrentHashMap<>();

    public LoanIssueStrategy getLoanIssueStrategy(String channel) {
        return STRATEGY_MAP.getOrDefault(channel, STRATEGY_MAP.get(LoanIssueChannelEnum.BAO_FU.getCode()));
    }

}
