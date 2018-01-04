package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starlightfinancial.deductiongateway.baofu.rsa.RsaCodingUtil;
import org.starlightfinancial.deductiongateway.baofu.util.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2017-7-22.
 */
@RestController
public class PayGetBgController {
    private static final Logger log = LoggerFactory.getLogger(PayGetBgController.class);

    @RequestMapping("/PayGetBgAsyn")
    public String UpdateDeduction(HttpServletRequest req) throws IOException {
        System.out.println("I'm in");
        String cerpath = "E:\\bfkey_100000276@@100000990.cer";

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
            dataContent = RsaCodingUtil.decryptByPubCerFile(req.getParameter("data_content"), cerpath);
            dataContent = SecurityUtil.Base64Decode(dataContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("returnData" + dataContent);
        return "OK";
    }
}
