package org.starlightfinancial.deductiongateway.web;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starlightfinancial.deductiongateway.BaofuConfig;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by Administrator on 2017-7-22.
 */
@RestController
public class BaoFuPayGetBgController {
    private static final Logger log = LoggerFactory.getLogger(BaoFuPayGetBgController.class);

    @Autowired
    private MortgageDeductionService mortgageDeductionService;
    @Autowired
    private BaofuConfig baofuConfig;


    @RequestMapping("/BaoFuPayGetBgAsyn")
    public String UpdateDeduction(HttpServletRequest req) throws IOException {
        System.out.println("I'm in");

        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
        }

        String dataContent = null;
        try {
            System.out.println("返回的加密数据:" + req.getParameter("data_content"));
            dataContent = RsaCodingUtil.decryptByPubCerFile(req.getParameter("data_content"), baofuConfig.getClassicCerFile());
            String returnData = SecurityUtil.Base64Decode(dataContent);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            log.info("宝付返回的数据:" + jsonObject.toJSONString()+"\n");
            String ordId = jsonObject.getObject("trans_id", String.class);
            String resp_code = jsonObject.getObject("resp_code", String.class);
            String valueByCode = BFErrorCodeEnum.getValueByCode(resp_code);
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(ordId);
            if (mortgageDeduction != null) {
                //BF00338 分账成功
                if (valueByCode != null && StringUtils.equals("分账成功", valueByCode)) {
                    mortgageDeduction.setLedgerState("1");
                } else {
                    mortgageDeduction.setLedgerState("0");
                }
                mortgageDeductionService.updateMortgageDeductions(Arrays.asList(mortgageDeduction));
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("宝付后台通知数据更新失败", e);
        }


        return "OK";
    }
}
