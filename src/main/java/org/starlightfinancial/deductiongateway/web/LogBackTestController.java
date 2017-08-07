package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by senlin.deng on 2017-08-07.
 */
@Controller
public class LogBackTestController {
    private static Logger LOGGER = LoggerFactory.getLogger(LogBackTestController.class);
    @RequestMapping("/test.do")
    public void test (){
        LOGGER.debug("=======DEBUG=======");
        LOGGER.info("========INFO=======");
        LOGGER.warn("========WARN=======");
        LOGGER.error("=======ERROR=======");
    }
}
