package org.starlightfinancial.deductiongateway.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Senlin.Deng
 * @Description: 策略实现管理类
 * @date: Created in 2018/6/4 16:07
 * @Modified By:
 */
@Service
public class OperationStrategyContext {

    @Autowired
    private final Map<String, OperationStrategy> STRATEGY_MAP = new ConcurrentHashMap<>();

    /**
     * 根据策略键即代扣渠道代码获取具体策略
     * @param strategyKey 策略键
     * @return 返回具体策略
     */
    public OperationStrategy getOperationStrategy(String strategyKey) {
        return STRATEGY_MAP.getOrDefault(strategyKey, STRATEGY_MAP.get("0001"));
    }

    /**
     * 计算手续费
     *
     * @param mortgageDeduction 代扣信息
     */
    public void calculateHandlingCharge(MortgageDeduction mortgageDeduction) {
        OperationStrategy operationStrategy = STRATEGY_MAP.get(mortgageDeduction.getChannel());
        operationStrategy.calculateHandlingCharge(mortgageDeduction);
    }
}
