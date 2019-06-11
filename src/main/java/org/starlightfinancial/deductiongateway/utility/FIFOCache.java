package org.starlightfinancial.deductiongateway.utility;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 先进先出缓存.
 * @date: Created in 2019/6/11 9:49
 * @Modified By:
 */
public class FIFOCache<K,V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 5257665931783053353L;
    private final int SIZE;
    public FIFOCache(int size) {
        super();
        SIZE = size;
    }
    /**
     * 重写淘汰机制
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        //如果缓存存储达到最大值删除最先进入的元素
        return size() > SIZE;
    }

}