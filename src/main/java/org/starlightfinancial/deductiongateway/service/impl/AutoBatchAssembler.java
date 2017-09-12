package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.UnionPayUtil;

import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AutoBatchAssembler extends Assembler {

    @Override
    public void assembleMessage() throws Exception {
        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        for (AutoBatchDeduction autoBatchDeduction : list) {
            GoPayBean goPayBean = autoBatchDeduction.transToGoPayBean();
            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer());
            goPayBean.setChkValue(chkValue);
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                throw new Exception("银联报文签名异常");
            }
            getResult().add(goPayBean);
            System.out.println(goPayBean);
        }
    }
}
