package org.starlightfinancial.deductiongateway.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private final Map<String, OperationStrategy> STRATEGYMAP = new ConcurrentHashMap<>();

    public OperationStrategy getOperationStrategy(String strategyKey)  {
        return STRATEGYMAP.getOrDefault(strategyKey, STRATEGYMAP.get("0001"));
    }
}
