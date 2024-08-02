package com.dongsoj.dongsojbackendjudgeservice.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Bean的方式来初始化RabbitMQ的连接和创建
 * @author dongsoj
 */
@Slf4j
@Component
public class InitRabbitMqBean {

    @Value("${spring.rabbitmq.host:localhost}")
    private String host;

    @PostConstruct
    public void doInit(){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = "code_queue";
//            Map<String, Object> codeMap = new HashMap<>();

            // 将队列绑定好死信交换机
//            codeMap.put("x-dead-letter-exchange", "code_dead_exchange");
//            codeMap.put("x-dead-letter-routing-key", "my_dead_routingKey");
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, "my_routingKey");
//
//            // 创建死信队列和死信交换机
//            // 创建死信队列
//            channel.queueDeclare("code_dead_queue", true, false, false, null);
//            // 创建死信交换机
//            channel.exchangeDeclare("code_dead_exchange", "direct");
//            channel.queueBind("code_dead_queue", "code_dead_exchange", "my_dead_routingKey");
            log.info("消息队列启动成功");
        } catch (Exception e) {
            log.error("消息队列启动失败");
        }
    }
}
