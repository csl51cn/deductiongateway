package org.starlightfinancial.deductiongateway.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.utility.CalMD5;

import javax.servlet.http.HttpSession;

/**
 * 代扣账户数据管理Controller
 */
@Controller
public class MortgageDeductionController {

    /**
     *  代扣账户数据文件导入
     * @param uploadFile
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/upload.do",produces = MediaType.TEXT_PLAIN_VALUE +";charset=utf-8")
    @ResponseBody
    public  String   importFile(MultipartFile uploadFile, HttpSession session){
        String md5ByFile = CalMD5.getMd5ByFile(uploadFile);

        System.out.println(uploadFile.getName());
        System.out.println(uploadFile.getOriginalFilename());

         return  "1";
      // return  "0";
    }

    @RequestMapping(value="/mortgageDeductionController/queryDeductionResult.do",produces = MediaType.TEXT_PLAIN_VALUE +";charset=utf-8")
    @ResponseBody
    public String  queryDeductionResult(String datetimeStart,String datetimeEnd ,String customerName ){
        System.out.println(datetimeStart + ":" + ":" +datetimeEnd +":" +customerName);
        return  null;
    }
}
