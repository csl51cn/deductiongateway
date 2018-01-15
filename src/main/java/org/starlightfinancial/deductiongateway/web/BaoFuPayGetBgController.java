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
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
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
        ClassLoader classLoader = BaoFuPayGetBgController.class.getClassLoader();
        URL url = classLoader.getResource(baofuConfig.getCerFile());
//        String cerpath = "E:\\bfkey_100000276@@100000990.cer";
//        File cerfile = new File(cerpath);

        String  cerpath =  url.getPath();
        File cerfile = new File(cerpath);

        if (!cerfile.exists()) {//判断宝付公钥是否为空
            System.out.printf("公钥文件不存在！");
        }

        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
        }

        String dataContent = null;
        try {
            System.out.println("返回的数据:"+req.getParameter("data_content"));
            dataContent = RsaCodingUtil.decryptByPubCerFile(req.getParameter("data_content"), baofuConfig.getCerFile());
            String  returnData = SecurityUtil.Base64Decode(dataContent);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(returnData);
            String ordId = jsonObject.getObject("trans_id", String.class);
            String resp_code = jsonObject.getObject("resp_code", String.class);
            String valueByCode = BFErrorCodeEnum.getValueByCode(resp_code);
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(ordId);

            //BF00338 分账成功
            if(valueByCode !=null && StringUtils.equals("分账成功",valueByCode)){
                mortgageDeduction.setLedgerState("1");
            }else{
                mortgageDeduction.setLedgerState("0");
            }
            mortgageDeductionService.updateMortgageDeductions(Arrays.asList(mortgageDeduction));

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("returnData" + dataContent);
        return "OK";
    }
}
