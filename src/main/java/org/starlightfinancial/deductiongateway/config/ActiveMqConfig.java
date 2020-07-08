package org.starlightfinancial.deductiongateway.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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

    /**
     * 创建active mq连接工厂
     *
     * @param url              broker-url
     * @param userName         用户名
     * @param password         密码
     * @param redeliveryPolicy 重发策略
     * @return
     */
    private ActiveMQConnectionFactory createActiveMqConnectionFactory(String url, String userName, String password, RedeliveryPolicy redeliveryPolicy) {
        ActiveMQConnectionFactory activeMqConnectionFactory =
                new ActiveMQConnectionFactory(
                        userName,
                        password,
                        url);
        activeMqConnectionFactory.setRedeliveryPolicy(redeliveryPolicy);
        return activeMqConnectionFactory;
    }

    /**
     * 创建 JmsTemplate
     *
     * @param mqConnectionFactory 连接工厂
     * @return
     */
    private JmsTemplate createJmsTemplate(ActiveMQConnectionFactory mqConnectionFactory) {
        JmsTemplate jmsTemplate = new JmsTemplate();
        //进行持久化配置 1表示非持久化，2表示持久化
        jmsTemplate.setDeliveryMode(2);
        jmsTemplate.setConnectionFactory(mqConnectionFactory);
        //客户端签收模式
        jmsTemplate.setSessionAcknowledgeMode(4);
        return jmsTemplate;
    }

    /**
     * 定义一个消息监听器容器工厂，这里定义的是点对点模式的监听器容器工厂
     *
     * @param mqConnectionFactory
     * @return
     */
    private DefaultJmsListenerContainerFactory createDefaultJmsListenerContainerFactory(ActiveMQConnectionFactory mqConnectionFactory) {
        DefaultJmsListenerContainerFactory factory =
                new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(mqConnectionFactory);
        //设置连接数
        factory.setConcurrency("1-10");
        //重连间隔时间
        factory.setRecoveryInterval(1000L);
        factory.setSessionAcknowledgeMode(4);
        return factory;
    }


    @Bean(name = "localActiveMQConnectionFactory")
    @Primary
    public ActiveMQConnectionFactory activeMQConnectionFactory(@Value("${spring.local.activemq.broker-url}") String url,
                                                               @Value("${spring.local.activemq.user}") String userName,
                                                               @Value("${spring.local.activemq.password}") String password,
                                                               RedeliveryPolicy redeliveryPolicy) {
        return createActiveMqConnectionFactory(url, userName, password, redeliveryPolicy);
    }

    @Bean(name = "localJmsTemplate")
    @Primary
    public JmsTemplate jmsTemplate(@Qualifier("localActiveMQConnectionFactory") ActiveMQConnectionFactory activeMQConnectionFactory) {
        return createJmsTemplate(activeMQConnectionFactory);
    }

    /***
     * 定义一个消息监听器连接工厂，这里定义的是点对点模式的监听器连接工厂
     * @param activeMQConnectionFactory
     * @return
     */
    @Bean(name = "localJmsQueueListener")
    @Primary
    public DefaultJmsListenerContainerFactory jmsQueueListenerContainerFactory(@Qualifier("localActiveMQConnectionFactory") ActiveMQConnectionFactory activeMQConnectionFactory) {
        return createDefaultJmsListenerContainerFactory(activeMQConnectionFactory);
    }

    /**
     * 创建云服务器 MQ 的连接工厂
     *
     * @param url
     * @param userName
     * @param password
     * @param redeliveryPolicy
     * @return
     */
    @Bean(name = "remoteActiveMQConnectionFactory")
    public ActiveMQConnectionFactory remoteActiveMqConnectionFactory(@Value("${spring.remote.activemq.broker-url}") String url,
                                                                     @Value("${spring.remote.activemq.user}") String userName,
                                                                     @Value("${spring.remote.activemq.user}") String password, RedeliveryPolicy redeliveryPolicy) {
        return createActiveMqConnectionFactory(url, userName, password, redeliveryPolicy);
    }

    /**
     * 创建服务器 MQ 的 JmsTemplate
     *
     * @param mqConnectionFactory
     * @return
     */
    @Bean(name = "remoteJmsTemplate")
    public JmsTemplate remoteJmsTemplate(@Qualifier("remoteActiveMQConnectionFactory") ActiveMQConnectionFactory mqConnectionFactory) {
        return createJmsTemplate(mqConnectionFactory);
    }

    /**
     * 创建服务器 MQ 的监听器容器工厂
     *
     * @param mqConnectionFactory
     * @return
     */
    @Bean(name = "remoteJmsQueueListener")
    public DefaultJmsListenerContainerFactory remoteJmsQueueListenerContainerFactory(@Qualifier("remoteActiveMQConnectionFactory") ActiveMQConnectionFactory mqConnectionFactory) {
        return createDefaultJmsListenerContainerFactory(mqConnectionFactory);
    }


}
