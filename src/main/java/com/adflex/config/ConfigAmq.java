package com.adflex.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Session;

@Configuration
public class ConfigAmq {

  @Value("${spring.activemq.broker-url}")
  private String brokerUrl;

  @Value("${spring.activemq.user}")
  private String username;

  @Value("${spring.activemq.password}")
  private String password;

  @Value("${activemq.job.mail-queue}")
  private String defaultDestination;

  @Bean
  public ActiveMQConnectionFactory connectionFactory() {
    ActiveMQConnectionFactory amqConnectionFactory = new ActiveMQConnectionFactory();
    amqConnectionFactory.setBrokerURL(brokerUrl);
    amqConnectionFactory.setUserName(username);
    amqConnectionFactory.setPassword(password);
    return amqConnectionFactory;
  }

  @Bean
  @Primary
  public CachingConnectionFactory cachingConnectionFactory() {
    CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
    cachingConnectionFactory.setSessionCacheSize(10);
    return cachingConnectionFactory;
  }

  @Bean
  public JmsTemplate orderJmsTemplate() {
    JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactory());
    jmsTemplate.setDefaultDestination(new ActiveMQQueue(defaultDestination));
    jmsTemplate.setReceiveTimeout(1500);
    jmsTemplate.setSessionAcknowledgeMode(Session.AUTO_ACKNOWLEDGE);
    return jmsTemplate;
  }
}