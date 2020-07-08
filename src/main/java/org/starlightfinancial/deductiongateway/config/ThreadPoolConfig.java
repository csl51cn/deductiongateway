package org.starlightfinancial.deductiongateway.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/4/4 11:06
 * @Modified By:
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 核心线程数 = CPU核心数 + 1
     */
    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;


    @Bean("taskExecutor")
    public ThreadPoolExecutor taskExecutor() {

        UserThreadFactory userThreadFactory = new UserThreadFactory("taskExecutor");

        return new ThreadPoolExecutor(CORE_POOL_SIZE, 20,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(50), userThreadFactory);

    }

    static class UserThreadFactory implements ThreadFactory {

        private final String namePrefix;
        private final AtomicInteger nextId = new AtomicInteger(1);

        UserThreadFactory(String whatFeatureOfGroup) {
            namePrefix = whatFeatureOfGroup + "-worker-";
        }

        /**
         * Constructs a new {@code Thread}.  Implementations may also initialize
         * priority, name, daemon status, {@code ThreadGroup}, etc.
         *
         * @param r a runnable to be executed by new thread instance
         * @return constructed thread, or {@code null} if the request to
         * create a thread is rejected
         */
        @Override
        public Thread newThread(Runnable r) {
            String name = namePrefix + nextId.getAndIncrement();
            return new Thread(null, r, name);
        }
    }


}
