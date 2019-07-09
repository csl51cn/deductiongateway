package org.starlightfinancial.deductiongateway.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

/**
 * @author: Senlin.Deng
 * @Description: ActiveMQ 配置
 * @date: Created in 2018/5/16 14:21
 * @Modified By:
 */
@EnableJms
@Configuration
public class ActiveMqConfig {
    @Bean
    public Queue queue() {
        return new ActiveMQQueue("chinaPayQueue");
    }

    @Bean
    public RedeliveryPolicy redeliveryPolicy() {
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        //是否在每次尝试重新发送失败后,增长这个等待时间
        redeliveryPolicy.setUseExponentialBackOff(true);
        //重发次数,默认为6次   这里设置为10次
        redeliveryPolicy.setMaximumRedeliveries(10);
        //第一次失败后重新发送之前等待时间,默认有+/-15%的误差collisionAvoidanceFactor,重发时间间隔,默认为1秒,设置单位为毫秒
        redeliveryPolicy.setInitialRedeliveryDelay(1);
        //第一次失败后重新发送之前等待x毫秒,第二次失败再等待x * 2毫秒,这里的2就是value,第三次失败在等待x * 2 * 2 毫秒
        redeliveryPolicy.setBackOffMultiplier(2);
        //是否避免消息碰撞
        redeliveryPolicy.setUseCollisionAvoidance(false);
        //设置重发最大拖延时间-1 表示没有拖延只有UseExponentialBackOff(true)为true时生效
        redeliveryPolicy.setMaximumRedeliveryDelay(-1);
        return redeliveryPolicy;
    }

    @Bean
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Value("${spring.activemq.broker-url}") String url, RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory activeMQConnectionFactory =
                new ActiveMQConnectionFactory(
                        "admin",
                        "admin",
                        url);
        activeMQConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMQConnectionFactory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ActiveMQConnectionFactory activeMQConnectionFactory, Queue queue) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        //进行持久化配置 1表示非持久化，2表示持久化
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setConnectionFactory(activeMQConnectionFactory);
        //此处可不设置默认，在发送消息时也可设置队列
        jmsTemplate.setDefaultDestination(queue);
        //客户端签收模式
        jmsTemplate.setSessionAcknowledgeMode(4);
        return jmsTemplate;
    }

    /***
     * 定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean(name = "jmsQueueListener")
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(ActiveMQConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(activeMQConnectionFactory);
        //设置连接数
        factory.setConcurrency("1-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(4);
        return factory;
    }
}
