package org.starlightfinancial.deductiongateway.domain.local;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.starlightfinancial.deductiongateway.utility.ServletUtil;
import org.starlightfinancial.deductiongateway.utility.Utility;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/5/16 10:03
 * @Modified By:
 */
@Configuration
public class UserAuditorAware  implements AuditorAware<Integer> {
    /**
     * Returns the current auditor of the application.
     *
     * @return the current auditor
     */
    @Override
    public Integer getCurrentAuditor() {
        return Utility.getLoginUserId(ServletUtil.getSession());
    }
}
