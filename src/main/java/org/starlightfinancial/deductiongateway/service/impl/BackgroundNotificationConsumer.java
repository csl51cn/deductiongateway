package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.enums.UnionPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;


/**
 * @author: Senlin.Deng
 * @Description: 支付结果后台通知消息消费者
 * @date: Created in 2018/5/16 14:29
 * @Modified By:
 */
@Component
public class BackgroundNotificationConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BackgroundNotificationConsumer.class);

    @Autowired
    private MortgageDeductionService mortgageDeductionService;

    /**
     * 银联支付结果后台通知处理
     *
     * @param textMessage
     * @param session
     */
    @JmsListener(destination = "chinaPayQueue", containerFactory = "jmsQueueListener")
    public void receiveQueue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            logger.info("收到银联后台通知消息:{}",text);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(text);
            //获取订单号
            String merOrderNo = jsonObject.getString("MerOrderNo");
            //根据订单号查询记录
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(merOrderNo);
            if (mortgageDeduction != null) {
                //0000表示支付成功,0001表示未支付,其余为失败
                if (StringUtils.equals("0000", jsonObject.getString("OrderStatus"))) {
                    //设置支付状态:1表示成功
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(jsonObject.getString("OrderStatus"));
                    mortgageDeduction.setErrorResult("支付成功");
                } else {
                    if (!StringUtils.equals("0001", jsonObject.getString("OrderStatus"))) {
                        mortgageDeduction.setIssuccess("0");
                    }
                    mortgageDeduction.setResult(jsonObject.getString("OrderStatus"));
                    mortgageDeduction.setErrorResult(UnionPayReturnCodeEnum.getValueByCode(jsonObject.getString("OrderStatus")));
                }
                mortgageDeductionService.updateMortgageDeduction(mortgageDeduction);
                textMessage.acknowledge();
            } else {
                //数据库中未查询到此订单号相关记录,消息重发
                session.recover();
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.recover();
        }

    }

}
