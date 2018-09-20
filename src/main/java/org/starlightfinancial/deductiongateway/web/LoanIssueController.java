package org.starlightfinancial.deductiongateway.web;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.starlightfinancial.deductiongateway.domain.local.LoanIssueBasicInfo;
import org.starlightfinancial.deductiongateway.service.LoanIssueService;
import org.starlightfinancial.deductiongateway.service.impl.BackgroundNotificationConsumer;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.List;

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
     * 放款操作
     * @return
     */
    @JmsListener(destination = "loanIssueQueueDev", containerFactory = "jmsQueueListener")
    public  void loanIssue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            LOGGER.info("收到放款消息:{}", text);
            List<LoanIssueBasicInfo> loanIssueBasicInfos = JSON.parseArray(text, LoanIssueBasicInfo.class);
            loanIssueService.loanIssue(loanIssueBasicInfos);
            textMessage.acknowledge();
        }catch (JMSException e) {
            e.printStackTrace();
            session.recover();
        }


    }




}
