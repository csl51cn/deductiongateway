package org.starlightfinancial.deductiongateway.service;

import org.starlightfinancial.deductiongateway.domain.remote.ExemptInfo;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 豁免信息管理Service
 * @date: Created in 2020/2/20 17:15
 * @Modified By:
 */
public interface ExemptInfoService {

    /***
     * 保存豁免信息
     * @param exemptInfos 豁免信息
     */
    void save(List<ExemptInfo> exemptInfos);
}
