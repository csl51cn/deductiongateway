package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.local.ExtraProcessing;
import org.starlightfinancial.deductiongateway.utility.PageBean;

import java.util.List;

/**
 * 额外处理Service接口
 */
public interface ExtraProcessingService {
    PageBean queryAllLimit(PageBean pageBean);

    void updateProcessing(ExtraProcessing extraProcessing);

    List<ExtraProcessing> findProcessingByIds(String ids);
}
