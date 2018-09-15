package com.plain.springboot.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Autowired
    private ConnectionFactory connectionFactory;
    /**
     * 定义一个名为：oKong 的队列
     *
     * @return
     */
    @Bean
    public Queue okongQueue() {
        return new Queue("okong");
    }
}
