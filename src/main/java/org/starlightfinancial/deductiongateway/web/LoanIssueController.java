package org.starlightfinancial.deductiongateway.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.starlightfinancial.deductiongateway.common.WebSocketServer;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueQueryCondition;
import org.starlightfinancial.deductiongateway.enums.LoanIssueBankEnum;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.service.impl.BackgroundNotificationConsumer;
import org.starlightfinancial.deductiongateway.utility.PageBean;
import org.starlightfinancial.deductiongateway.utility.Utility;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: Senlin.Deng
 * @Description: 贷款发放管理Controller
 * @date: Created in 2018/9/18 16:28
 * @Modified By:
 */
@Controller
@RequestMapping("/loanIssueController")
public class LoanIssueController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundNotificationConsumer.class);

    @Autowired
    private LoanIssueService loanIssueService;


    /**
     * 监听业务系统发出的放款消息,入库
     *
     * @param textMessage 消息
     * @param session     会话session
     * @throws JMSException 获取消息异常时抛出
     */
    @JmsListener(destination = "loanIssueQueueDev", containerFactory = "jmsQueueListener")
    public void saveLoanIssueFromMessageQueue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            LOGGER.info("收到放款消息:{}", text);
//            List<LoanIssueBasicInfo> loanIssueBasicInfos = JSON.parseArray(text, LoanIssueBasicInfo.class);
//            loanIssueBasicInfos = loanIssueService.saveLoanIssueBasicInfo(loanIssueBasicInfos);
            try {
                WebSocketServer.sendInfo(text);
            } catch (IOException e) {
                e.printStackTrace();
            }
            textMessage.acknowledge();
            //目前不自动放款
//            loanIssueService.loanIssue(loanIssueBasicInfos);
//            LOGGER.info("代付交易请求完成");
        } catch (JMSException e) {
            LOGGER.error("自动放款操作异常", e);
            session.recover();
        }
    }

    /**
     * 根据条件查询记录
     *
     * @param pageBean                分页信息
     * @param loanIssueQueryCondition 查询条件
     * @return 查询到的记录
     */
    @RequestMapping("/queryLoanIssue.do")
    @ResponseBody
    public Map<String, Object> queryLoanIssue(PageBean pageBean, LoanIssueQueryCondition loanIssueQueryCondition) {
        PageBean result = loanIssueService.queryLoanIssue(pageBean, loanIssueQueryCondition);
        return Utility.pageBean2Map(result);
    }

    /**
     * 保存记录
     *
     * @param loanIssueBasicInfo 贷款发放信息
     * @param session            会话session
     * @return 保存情况
     */
    @RequestMapping("/saveLoanIssue.do")
    @ResponseBody
    public String saveLoanIssue(LoanIssueBasicInfo loanIssueBasicInfo, HttpSession session) {
        try {
            loanIssueBasicInfo.setCreateId(Utility.getLoginUserId(session));
            loanIssueService.saveLoanIssueBasicInfo(Collections.singletonList(loanIssueBasicInfo));
            return "1";
        } catch (Exception e) {
            LOGGER.error("保存贷款发放信息失败", e);
            return "0";
        }
    }


    /**
     * 获取所有收款银行id和名称
     *
     * @return 收款银行id和名称
     */
    @RequestMapping("/getAllBankNameId.do")
    @ResponseBody
    public String getAllBankNameId() {
        JSONArray jsonArray = new JSONArray();
        LoanIssueBankEnum[] values = LoanIssueBankEnum.values();
        Arrays.stream(values).forEach(loanIssueBankEnum -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code", loanIssueBankEnum.getCode());
            jsonObject.put("name", loanIssueBankEnum.getBaoFuBank());
            jsonArray.add(jsonObject);
        });
        return jsonArray.toJSONString();
    }


    @RequestMapping("/loanIssue.do")
    @ResponseBody
    public String loanIssue(String ids) {
        try {
            List<LoanIssueBasicInfo> loanIssueBasicInfos = loanIssueService.queryLoanIssueListByIds(ids);
            loanIssueService.loanIssue(loanIssueBasicInfos);
            return "1";
        } catch (Exception e) {
            LOGGER.error("代扣");
        }
        return "0";
    }

}
