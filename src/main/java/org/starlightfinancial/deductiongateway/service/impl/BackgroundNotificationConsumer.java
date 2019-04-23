package org.starlightfinancial.deductiongateway.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.starlightfinancial.deductiongateway.baofu.domain.BFErrorCodeEnum;
import org.starlightfinancial.deductiongateway.domain.local.MortgageDeduction;
import org.starlightfinancial.deductiongateway.enums.ChinaPayReturnCodeEnum;
import org.starlightfinancial.deductiongateway.service.MortgageDeductionService;
import org.starlightfinancial.deductiongateway.strategy.OperationStrategyContext;
import org.starlightfinancial.deductiongateway.utility.Constant;
import org.starlightfinancial.rpc.hessian.entity.cpcn.request.Notice2018Req;

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

    @Autowired
    private OperationStrategyContext operationStrategyContext;

    /**
     * 银联支付结果后台通知处理
     *
     * @param textMessage
     * @param session
     */
    @JmsListener(destination = "chinaPayQueueDev", containerFactory = "jmsQueueListener")
    public void receiveChinaPayQueue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            logger.info("收到银联后台通知消息:{}", text);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(text);
            //获取订单号
            String merOrderNo = jsonObject.getString("MerOrderNo");
            if (StringUtils.isBlank(merOrderNo)) {
                //如果获取到的消息中订单号为空,签收消息
                textMessage.acknowledge();
                return;
            }
            //根据订单号查询记录
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(merOrderNo);
            if (mortgageDeduction != null) {
                //0000表示支付成功,0001表示未支付,其余为失败
                if (StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_001.getCode(), jsonObject.getString("OrderStatus"))) {
                    //设置支付状态:1表示成功
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(jsonObject.getString("OrderStatus"));
                    mortgageDeduction.setErrorResult("支付成功");
                    //计算手续费
                    operationStrategyContext.calculateHandlingCharge(mortgageDeduction);
                } else {
                    if (!StringUtils.equals(ChinaPayReturnCodeEnum.CHINA_PAY_CODE_002.getCode(), jsonObject.getString("OrderStatus"))) {
                        mortgageDeduction.setIssuccess("0");
                    }
                    mortgageDeduction.setResult(jsonObject.getString("OrderStatus"));
                    mortgageDeduction.setErrorResult(ChinaPayReturnCodeEnum.getValueByCode(jsonObject.getString("OrderStatus")));
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

    /**
     * 宝付支付结果后台通知处理
     *
     * @param textMessage
     * @param session
     */
    @JmsListener(destination = "baofuQueueDev", containerFactory = "jmsQueueListener")
    public void receiveBaoFuQueue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            logger.info("收到宝付后台通知消息:{}", text);
            JSONObject jsonObject = (JSONObject) JSONObject.parse(text);
            //获取订单号
            String transId = jsonObject.getString("trans_id");
            if (StringUtils.isBlank(transId)) {
                //如果获取到的消息中订单号为空,签收消息
                textMessage.acknowledge();
                return;
            }
            //根据订单号查询记录
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(transId);
            if (mortgageDeduction != null) {
                //0000表示支付成功
                if (StringUtils.equals(BFErrorCodeEnum.BF00000.getCode(), jsonObject.getString("biz_resp_code"))) {
                    //设置支付状态:1表示成功
                    mortgageDeduction.setIssuccess("1");
                    mortgageDeduction.setResult(jsonObject.getString("biz_resp_code"));
                    mortgageDeduction.setErrorResult("支付成功");
                    //计算手续费
                    operationStrategyContext.calculateHandlingCharge(mortgageDeduction);
                } else {
                    mortgageDeduction.setIssuccess("0");
                    mortgageDeduction.setResult(jsonObject.getString("biz_resp_code"));
                    mortgageDeduction.setErrorResult(BFErrorCodeEnum.getValueByCode(jsonObject.getString("biz_resp_code")));
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


    /**
     * 中金支付结果后台通知处理
     *
     * @param textMessage
     * @param session
     */
//    @JmsListener(destination = "chinaPayClearNetQueue", containerFactory = "jmsQueueListener")
    @JmsListener(destination = "chinaPayClearNetQueueDev", containerFactory = "jmsQueueListener")
    public void receiveChinaPayClearNetQueue(TextMessage textMessage, Session session) throws JMSException {
        try {
            String text = textMessage.getText();
            logger.info("收到中金支付后台通知消息:{}", text);
            Notice2018Req notice2018Request = JSONObject.parseObject(text, Notice2018Req.class);
            //获取订单号
            String txSN = notice2018Request.getTxSN();
            if (StringUtils.isBlank(txSN)) {
                //如果获取到的消息中订单号为空,签收消息
                textMessage.acknowledge();
                return;
            }
            //根据订单号查询记录
            MortgageDeduction mortgageDeduction = mortgageDeductionService.findByOrdId(txSN);
            if (mortgageDeduction != null) {

                if (StringUtils.equals(mortgageDeduction.getIssuccess(), Constant.CHECK_SUCCESS)) {
                    //同步返回已经支付成功,不用更新状态,签收消息
                    textMessage.acknowledge();
                    return;
                }
                //30表示支付成功
                if (30 == notice2018Request.getStatus()) {
                    //设置支付状态:1表示成功
                    mortgageDeduction.setIssuccess(Constant.CHECK_SUCCESS);
                    mortgageDeduction.setResult(notice2018Request.getResponseCode());
                    mortgageDeduction.setErrorResult("支付成功");
                    //计算手续费
                    operationStrategyContext.calculateHandlingCharge(mortgageDeduction);
                } else if (40 == notice2018Request.getStatus()) {
                    //代扣失败
                    mortgageDeduction.setErrorResult(notice2018Request.getResponseMessage());
                    mortgageDeduction.setResult(notice2018Request.getResponseCode());
                    mortgageDeduction.setIssuccess(Constant.CHECK_FAILURE);
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
