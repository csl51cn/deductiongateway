package org.starlightfinancial.deductiongateway.web;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.common.Message;
import org.starlightfinancial.deductiongateway.common.SameUrlData;
import org.starlightfinancial.deductiongateway.domain.local.AccountManager;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.domain.local.SysUser;
import org.starlightfinancial.deductiongateway.service.ChannelDispatchService;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Senlin.Deng
 * @Description: 各渠道签约状态查询, 签约短信发送, 签约, 支付, 支付状态查询等操作管理Controller
 * @date: Created in 2018/6/4 16:04
 * @Modified By:
 */
@Controller
@RequestMapping("/channelDispatchController")
public class ChannelDispatchController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelDispatchController.class);
    @Autowired
    private ChannelDispatchService channelDispatchService;
    @Autowired
    private MortgageDeductionService mortgageDeductionService;

    /**
     * 查询是否签约
     *
     * @param id      记录id
     * @param channel 渠道
     * @return 返回包含查询结果的Message对象
     */
    @RequestMapping(value = "/queryIsSigned.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message queryIsSigned(Integer id, String channel) {
        Message message = channelDispatchService.queryIsSigned(id, channel);
        return message;
    }


    /**
     * 发送签约短信
     *
     * @param accountManager 代扣卡相关信息
     * @param channel        渠道
     * @return 返回包含短信发送结果的Message对象
     */
    @RequestMapping(value = "/sendSignSmsCode.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message sendSignSmsCode(AccountManager accountManager, String channel) {
        Message message = channelDispatchService.sendSignSmsCode(accountManager, channel);
        return message;
    }


    /**
     * 签约
     *
     * @param accountManager 代扣卡相关信息
     * @param channel        渠道
     * @return 返回签约结果的Message对象
     */
    @RequestMapping(value = "/sign.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message sign(AccountManager accountManager, String channel) {
        Message message = channelDispatchService.sign(accountManager, channel);
        return message;
    }

    /**
     * 执行代扣
     *
     * @param ids        代扣记录id
     * @param reGenerate 扣款结果页面发起的代扣需要重新生成一条记录,0表示不需要生成,1表示需要生成
     * @param channel    渠道
     * @param request    请求
     * @param session    会话session
     * @return 返回代扣执行情况
     */
    @RequestMapping(value = "/doPay.do")
    @SameUrlData
    @ResponseBody
    public String doPay(String ids, String reGenerate, String channel, HttpServletRequest request, HttpSession session) {
        try {
            if (StringUtils.isEmpty(ids)) {
                return "请选择一条记录进行代扣";
            }
            String ipAddress = Utility.getIpAddress(request);
            LOGGER.info("执行代扣,操作人员:{},发起请求ip:{},代扣记录ids:{},是(1)否(0)重新生成记录:{},代扣渠道:{}",
                    ((SysUser) session.getAttribute("loginUser")).getLoginName(), ipAddress, ids, reGenerate, channel);

            List<MortgageDeduction> list = mortgageDeductionService.findMortgageDeductionListByIds(ids);
            ArrayList<MortgageDeduction> mortgageDeductionList = new ArrayList<>();
            if (Constant.CHECK_SUCCESS.equals(reGenerate)) {
                for (MortgageDeduction oldMortgageDeduction : list) {
                    MortgageDeduction newMortgageDeduction = new MortgageDeduction();
                    BeanUtils.copyProperties(oldMortgageDeduction, newMortgageDeduction);
                    newMortgageDeduction.setId(null);
                    newMortgageDeduction.setOrdId(null);
                    newMortgageDeduction.setIssuccess("2");
                    newMortgageDeduction.setErrorResult(null);
                    newMortgageDeduction.setType("1");
                    newMortgageDeduction.setCreateDate(new Date());
                    newMortgageDeduction.setCheckState(null);
                    newMortgageDeduction.setIsUploaded("0");
                    mortgageDeductionList.add(newMortgageDeduction);
                }
                list = mortgageDeductionList;
            }
            channelDispatchService.doPay(list, channel);
            return Constant.CHECK_SUCCESS;
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
            return "代扣渠道不支持当前银行卡";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /**
     * 查询代扣结果
     *
     * @param id 要查询结果的记录id
     * @return 返回包含查询结果的Message对象
     */
    @RequestMapping(value = "/queryPayResult.do", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Message queryPayResult(Integer id) {
        Message message = channelDispatchService.queryPayResult(id);
        return message;
    }

    /**
     * 获取手续费最少的渠道
     *
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/getHandlingChargeLowestChannel.do")
    @ResponseBody
    public Message getHandlingChargeLowestChannel(Integer id) {
        return channelDispatchService.getHandlingChargeLowestChannel(id);
    }


    /**
     * 获取启用的支持当前记录银行的的渠道
     *
     * @param id 主键
     * @return
     */
    @RequestMapping(value = "/getEnabledChannel.do")
    @ResponseBody
    public Message getEnabledChannel(Integer id) {
        return channelDispatchService.getEnabledChannel(id);
    }


    /**
     * 注册账号
     *
     * @param id      accountManager主键
     * @param channel 渠道
     * @return
     */
    @RequestMapping(value = "/registration.do")
    @ResponseBody
    public Message registration(Integer id, String channel) {
        return channelDispatchService.registration(id, channel);
    }

}
