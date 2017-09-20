package org.starlightfinancial.deductiongateway.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.GoPayBean;
import org.starlightfinancial.deductiongateway.domain.local.SysDict;
import org.starlightfinancial.deductiongateway.domain.local.SysDictRepository;
import org.starlightfinancial.deductiongateway.domain.remote.AutoBatchDeduction;
import org.starlightfinancial.deductiongateway.service.Assembler;
import org.starlightfinancial.deductiongateway.utility.DictionaryType;
import org.starlightfinancial.deductiongateway.utility.UnionPayUtil;

import java.util.List;

/**
 * Created by sili.chen on 2017/9/1
 */
@Component
public class AutoBatchAssembler extends Assembler {

    @Autowired
    SysDictRepository sysDictRepository;

    @Override
    public void assembleMessage() throws Exception {

        List<AutoBatchDeduction> list = ((Splitter) this.route).getDeductionList();
        for (AutoBatchDeduction autoBatchDeduction : list) {
            GoPayBean goPayBean = autoBatchDeduction.transToGoPayBean();
            goPayBean.setParam1(handleBankName(goPayBean.getParam1()));
            goPayBean.setParam5(handleCertificateType(goPayBean.getParam5()));
            String chkValue = UnionPayUtil.sign(goPayBean.getMerId(), goPayBean.createStringBuffer());
            if (StringUtils.isEmpty(chkValue) || chkValue.length() != 256) {
                throw new Exception("银联报文签名异常");
            }
            goPayBean.setChkValue(chkValue);
            getResult().add(goPayBean);
            System.out.println(goPayBean);
        }
    }

    private String handleBankName(String bankName) {
        List<SysDict> openBankList = sysDictRepository.findByDicType(DictionaryType.MERID_SOURCE);
        for (SysDict sysDict : openBankList) {
            if (StringUtils.equals(bankName, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }
        return "";
    }

    private String handleCertificateType(String certificateType) {
        List<SysDict> cTypeLst = sysDictRepository.findByDicType(DictionaryType.CERTIFICATE_TYPE);
        for (SysDict sysDict : cTypeLst) {
            if (StringUtils.equals(certificateType, sysDict.getDicValue())) {
                return sysDict.getDicKey();
            }
        }
        return "";
    }
}
