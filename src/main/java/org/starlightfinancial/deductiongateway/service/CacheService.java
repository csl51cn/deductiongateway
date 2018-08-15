package org.starlightfinancial.deductiongateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.starlightfinancial.deductiongateway.domain.remote.BusinessTransaction;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;

import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 缓存服务
 * @date: Created in 2018/7/20 15:24
 * @Modified By:
 */
public final class CacheService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);
    private volatile static CacheService cacheService = null;

    public static void refresh() {
        cacheService = new CacheService();
        LOGGER.info("**********CacheService 刷新完成**********");
    }

    public static CacheService getInstance() {
        if (cacheService == null) {
            synchronized (CacheService.class) {
                if (cacheService == null) {
                    cacheService = new CacheService();
                    LOGGER.info("**********CacheService 初始化完成**********");
                }
            }
        }
        return cacheService;
    }

    private static Map<String, BusinessTransaction> businessTransactionCacheMap;

    private BusinessTransactionService businessTransactionService = (BusinessTransactionService) SpringContextUtil.getBean("businessTransactionService");

    private CacheService() {
        loadBusinessTransactionCacheMap();
    }

    private void loadBusinessTransactionCacheMap() {
        businessTransactionCacheMap = businessTransactionService.findAll();
    }


    public static Map<String, BusinessTransaction> getBusinessTransactionCacheMap() {
        return businessTransactionCacheMap;
    }


}
