package org.starlightfinancial.deductiongateway.utility;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.starlightfinancial.deductiongateway.config.ChinaPayClearNetConfig;
import org.starlightfinancial.rpc.hessian.entity.common.RequestResult;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.Tx2011Req;
import org.starlightfinancial.rpc.hessian.service.cpcn.BaseService;

/**
 * @author: Senlin.Deng
 * @Description:
 * @date: Created in 2019/10/24 10:46
 * @Modified By:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HessianProxyFactoryUtilsTest {

    @Autowired
    private ChinaPayClearNetConfig chinaPayClearNetConfig;

    @Test
    public void getHessianClientBean() {
        Tx2011Req tx2011Req = new Tx2011Req();
        tx2011Req.setAccountName("");
        tx2011Req.setAccountNumber("");
        tx2011Req.setBranchName("");
        tx2011Req.setProvince("");
        tx2011Req.setAccountType(Integer.parseInt("11"));
        tx2011Req.setCity("");
        tx2011Req.setNote("");
        tx2011Req.setEmail("");
        tx2011Req.setIdentificationNumber("");
        tx2011Req.setIdentificationType("0");
        tx2011Req.setContractUserID("");
        tx2011Req.setCardMediaType("10");
        tx2011Req.setBankNoByPBC("");
        tx2011Req.setReserve1("");
        //银行编码
        tx2011Req.setBankID("105");
        tx2011Req.setInstitutionID("000685");
        //设置订单号
        tx2011Req.setTxSN(MerSeq.tickOrder());
        tx2011Req.setAmount(2);
        tx2011Req.setSplitType("30");
        tx2011Req.setSettlementFlag("0001_1,0004_1");
        tx2011Req.setPhoneNumber("");
        System.out.println(JSONObject.toJSONString(tx2011Req));
        BaseService tx2011Service = null;
        try {
            tx2011Service = HessianProxyFactoryUtils.getHessianClientBean(BaseService.class, chinaPayClearNetConfig.getClassicDeductionUrl());
            RequestResult requestResult = tx2011Service.doBusiness(tx2011Req);
            System.out.println(requestResult);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}