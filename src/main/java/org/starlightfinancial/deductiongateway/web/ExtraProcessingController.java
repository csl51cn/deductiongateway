package org.starlightfinancial.deductiongateway.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.domain.local.ExtraProcessing;
import org.starlightfinancial.deductiongateway.service.ExtraProcessingService;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 额外处理Controller
 */
@Controller
@RequestMapping("/extraProcessingController")
public class ExtraProcessingController {

    @Autowired
    private ExtraProcessingService extraProcessingService;


    /**
     * 查询所有的额外处理
     * @param pageBean
     * @return
     */
    @RequestMapping(value = "/queryProcessiong.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, Object> queryLimit(PageBean pageBean) {
        PageBean result = extraProcessingService.queryAllLimit(pageBean);
        return Utility.pageBean2Map(result);
    }


    /**
     *
     * @param ids
     * @return
     */
    @RequestMapping(value = "/updateProcessing.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String updateProcessing(String ids) {
        try {
            List<ExtraProcessing> list = extraProcessingService.findProcessingByIds(ids);
            for (ExtraProcessing extraProcessing : list){
                extraProcessing.setModifyDate(new Date());//设置修改时间
                if("1".equals(extraProcessing.getStatus())){//修改自动代扣状态:0关闭,1开启
                    extraProcessing.setStatus("0");
                }else{
                    extraProcessing.setStatus("1");
                }
                extraProcessingService.updateProcessing(extraProcessing);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
        return "1";
    }


}
