package org.starlightfinancial.deductiongateway.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.model.SysUser;
import org.starlightfinancial.deductiongateway.utility.CalMD5;

import javax.servlet.http.HttpSession;

/**
 * 代扣账户数据文件导入管理Controller
 */
@Controller
public class MortgageDeductionUploadController {

    @RequestMapping(value = "/mortgageDeductionUploadController/upload.do",produces = MediaType.TEXT_PLAIN_VALUE +";charset=utf-8")
    @ResponseBody
    public  String   importFile(MultipartFile   uploadFile, HttpSession session){
        String md5ByFile = CalMD5.getMd5ByFile(uploadFile);
        SysUser loginUser= (SysUser) session.getAttribute("user");

        System.out.println(uploadFile.getName());
        System.out.println(uploadFile.getOriginalFilename());

        return  "文件导入成功";
    }
}
