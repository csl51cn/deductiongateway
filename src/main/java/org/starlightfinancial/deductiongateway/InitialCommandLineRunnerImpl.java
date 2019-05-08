package org.starlightfinancial.deductiongateway;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.service.CacheService;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2018/7/23 11:08
 * @Modified By:
 */
@Component
public class InitialCommandLineRunnerImpl implements CommandLineRunner {
    /**
     * Callback used to run the bean.
     *
     * @param args incoming main method arguments
     * @throws Exception on error
     */
    @Override
    public void run(String... args) throws Exception {
        //初始化
        CacheService.getInstance();
    }
}
