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

    private final Map<String, OperationStrategy> strategyMap = new ConcurrentHashMap<>();


    @Autowired
    public OperationStrategyContext(Map<String, OperationStrategy> strategyMap) {
        this.strategyMap.clear();
        strategyMap.forEach((k, v)-> this.strategyMap.put(k, v));
    }


    public OperationStrategy getOperationStrategy (String strategyKey){
        if (strategyMap.containsKey(strategyKey)){
            return strategyMap.get(strategyKey);
        }else{
            return null;
        }
    }
}
