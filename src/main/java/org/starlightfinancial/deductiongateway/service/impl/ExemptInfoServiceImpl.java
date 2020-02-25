package org.starlightfinancial.deductiongateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.starlightfinancial.deductiongateway.domain.remote.ExemptInfo;
import org.starlightfinancial.deductiongateway.domain.remote.ExemptInfoRepository;
import org.starlightfinancial.deductiongateway.service.ExemptInfoService;

import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2020/2/20 17:15
 * @Modified By:
 */
@Service
public class ExemptInfoServiceImpl implements ExemptInfoService {
    @Autowired
    private ExemptInfoRepository exemptInfoRepository;


    /***
     * 保存豁免信息
     * @param exemptInfos 豁免信息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(List<ExemptInfo> exemptInfos) {
        exemptInfoRepository.save(exemptInfos);
    }
}
