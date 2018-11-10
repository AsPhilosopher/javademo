package com.plain.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class RabbitMqDemo {
    private byte[] messageBodyBytes = "Hello, world!".getBytes();

    private String myConsumerTag = "myConsumerTag";
    private String myExchange = "myExchange";
    private String directExchange = "direct";
    private String routeKey = "routeKey";
    private String queueName = "okong";

    private Connection connection;
    private Channel channel;

    @Before
    public void before() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        connection = factory.newConnection();
        System.out.println(connection);
        channel = connection.createChannel();
    }

    @Test
    public void push() throws IOException {
//        channel.exchangeDeclare(myExchange, directExchange, true); //声明一个exchange
//        String queueName = channel.queueDeclare().getQueue(); //生成一个随机的名字
//        System.out.println(queueName);
//        channel.queueDeclare("myQueueName", true, false, false, null); //自定义queue name
//        channel.queueBind(queueName, myExchange, routeKey); //绑定队列与exchange

        channel.basicPublish(myExchange, routeKey, null, messageBodyBytes);

        boolean autoAck = false;
        channel.basicConsume(queueName, autoAck, myConsumerTag,
                new DefaultConsumer(channel) {
                    @Override
                    public void handleDelivery(String consumerTag,
                                               Envelope envelope,
                                               AMQP.BasicProperties properties,
                                               byte[] body)
                            throws IOException {
                        System.out.println("consumerTag: " + consumerTag);
                        String routingKey = envelope.getRoutingKey();
                        System.out.println("routingKey: " + routingKey);
                        String contentType = properties.getContentType();
                        System.out.println("contentType: " + contentType);
                        long deliveryTag = envelope.getDeliveryTag();
                        System.out.println("deliveryTag: " + deliveryTag);
                        // (process the message components here ...)
                        System.out.println("body: " + new String(body));
                        //这里做确认操作，不确认的话，channel关闭则消息又回到ready状态
                        channel.basicAck(deliveryTag, false);
                    }
                });
        channel.basicCancel(myConsumerTag);
    }

    @Test
    public void pull() throws IOException {
        Map<String, Object> headers = new HashMap<>();
        headers.put("Test", "TEST");
        AMQP.BasicProperties basicProperties = new AMQP.BasicProperties(null, null, headers, null, null, null, null, null, null, null, null, null, null, null);
        channel.basicPublish(myExchange, routeKey, basicProperties, messageBodyBytes);

        boolean autoAck = false;
        GetResponse response = channel.basicGet(queueName, autoAck);
        if (response == null) {
            // No message retrieved.
            System.out.println("No message retrieved.");
        } else {
            AMQP.BasicProperties props = response.getProps();
            System.out.println("headers: " + props.getHeaders());
            byte[] body = response.getBody();
            System.out.println("body: " + new String(body));
            long deliveryTag = response.getEnvelope().getDeliveryTag();
            System.out.println("deliveryTag: " + deliveryTag);
            channel.basicAck(deliveryTag, false); // acknowledge receipt of the message
        }

        response = channel.basicGet(queueName, autoAck);
        if (response == null) {
            // No message retrieved.
            System.out.println("No message retrieved.");
        }
    }

    @After
    public void after() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
