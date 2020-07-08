package org.starlightfinancial.deductiongateway.async;


import lombok.extern.slf4j.Slf4j;
import org.starlightfinancial.deductiongateway.utility.SpringContextUtil;
import org.starlightfinancial.deductiongateway.utility.ThreadUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/1/14 13:49
 * @Modified By:
 */
@Slf4j
public class AsyncManager {

    /**
     * 异步操作任务调度线程池
     */
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) SpringContextUtil.getBean("taskExecutor");


    /**
     * 单例
     */
    private static AsyncManager asyncManager = new AsyncManager();

    private AsyncManager() {
    }


    public static AsyncManager getInstance() {
        return asyncManager;
    }


    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(Runnable task) {
        executor.submit(task);
    }


    /**
     * 停止任务线程池
     */
    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }

    public boolean hasWork() {
        int activeCount = executor.getActiveCount();
        return activeCount > 0;
    }

}
