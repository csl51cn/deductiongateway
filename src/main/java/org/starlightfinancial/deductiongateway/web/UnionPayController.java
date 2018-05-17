package org.starlightfinancial.deductiongateway.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.service.ReconciliationService;

import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sili.chen on 2017/9/14
 */
@Controller
public class UnionPayController {

    private static final Logger log = LoggerFactory.getLogger(UnionPayController.class);

    @Autowired
    private ReconciliationService reconciliationService;

    /**
     * 银联订单明细文件导入
     *
     * @param uploadFile
     * @param session
     * @return
     */
    @RequestMapping(value = "/unionPayController/upload.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Map<String, Object> importFile(MultipartFile uploadFile, HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        try {
            reconciliationService.unionPayDataImport(uploadFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        map.put("result", "1");
        map.put("msg", uploadFile.getOriginalFilename());
        return map;
    }


    @RequestMapping(value = "/unionPayController/bgNotification")
    @ResponseBody
    public String bgNotification(HttpServletRequest req) {
        try {
            req.setCharacterEncoding("utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException");
        }
        Map<String, String[]> parameterMap = req.getParameterMap();
        parameterMap.forEach((k, v) -> System.out.println("key:value = " + k + ":" + Arrays.toString(v)));
        return "ok";
    }




}
