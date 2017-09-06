package org.starlightfinancial.deductiongateway.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.service.impl.AutoBatchAssembler;
import org.starlightfinancial.deductiongateway.service.impl.ManualBatchAssembler;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AssemblerFactory {

    @Autowired
    private AutoBatchAssembler autoBatchAssembler;

    @Autowired
    private ManualBatchAssembler manualBatchAssembler;

    public Assembler getAssembleImpl(String type) throws Exception {
        if (StringUtils.equals("auto", type)) {
            return autoBatchAssembler;
        } else if (StringUtils.equals("manual", type)) {
            return manualBatchAssembler;
        } else {
            throw new Exception("报文组装实例工厂创建失败");
        }
    }
}
