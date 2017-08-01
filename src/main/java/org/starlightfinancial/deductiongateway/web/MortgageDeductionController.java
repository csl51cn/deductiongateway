package org.starlightfinancial.deductiongateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.starlightfinancial.deductiongateway.domain.MortgageDeduction;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.CalMD5;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 代扣账户数据管理Controller
 */
@Controller
public class MortgageDeductionController {

    @Autowired
    private MortgageDeductionService mortgageDeductionService;


    /**
     * 代扣账户数据文件导入
     *
     * @param uploadFile
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/upload.do", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=utf-8")
    @ResponseBody
    public String importFile(MultipartFile uploadFile, HttpSession session) {
        String md5ByFile = CalMD5.getMd5ByFile(uploadFile);

     // LoginUser loginUser =  session.getAttribute("loginUser");
      //  mortgageDeductionService.importCustomerData(uploadFile,getLoginUserId(session));
        System.out.println(uploadFile.getName());
        System.out.println(uploadFile.getOriginalFilename());

        return "1";
        // return  "0";
    }

    /**
     * 查询代扣数据
     *
     * @param startDate
     * @param endDate
     * @param customerName
     * @param pageBean
     * @param type 0:已执行代扣的数据 1:未执行
     * @param session
     * @return
     */
    @RequestMapping(value = "/mortgageDeductionController/queryDeductionData.do")
    @ResponseBody
    public Map<String,Object> queryDeductionData(Date startDate, Date endDate, String customerName, PageBean pageBean, String  type, HttpSession session) {
        System.out.println(startDate + ":" +  endDate + ":" + customerName + ":" + pageBean);
        endDate = Utility.addDay(endDate, 1);
        PageBean result = mortgageDeductionService.queryDeductionData(startDate, endDate, customerName, pageBean, type, 14);
        return Utility.pageBean2Map(pageBean);
    }


    @RequestMapping(value="/mortgageDeductionController/saveMortgageDeductions.do")
    public  String  saveMortgageDeductions (List<MortgageDeduction> list){
        System.out.println(list);
        return null;
    }

    private int getLoginUserId ( HttpSession session){
        // LoginUser loginUser =  session.getAttribute("loginUser");

        return 0;
    }
}
