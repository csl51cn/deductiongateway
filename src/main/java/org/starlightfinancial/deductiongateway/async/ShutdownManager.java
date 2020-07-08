package org.starlightfinancial.deductiongateway.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @author: Senlin.Deng
 * @Description: 确保应用退出时能关闭后台线程
 * @date: Created in 2019/1/14 13:47
 * @Modified By:
 */
@Slf4j
@Component
public class ShutdownManager {

    @PreDestroy
    public void destroy() {
        shutdownAsyncManager();
    }

    /**
     * 停止异步任务
     */
    private void shutdownAsyncManager() {
        try {
            log.info("====关闭后台任务任务线程池====");
            AsyncManager.getInstance().shutdown();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
