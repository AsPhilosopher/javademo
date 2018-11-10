package com.plain.springboot.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
//@RabbitListener 监听 okong 队列
@RabbitListener(queues = "okong")
public class Consumer {
    private Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * @param message
     * @RabbitHandler 指定消息的处理方法
     */
    @RabbitHandler
    public void process(byte[] message) {
        logger.info("接收的消息为: {}", new String(message));
    }
}
