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
        basicConsume();
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

    @Test
    public void transaction() throws IOException {
        channel.txSelect();
        try {
            channel.basicPublish(myExchange, routeKey, null, messageBodyBytes);
//            int a = 10 / 0;
            channel.txCommit();
        } catch (IOException e) {
            e.printStackTrace();
            channel.txRollback();
        } catch (Exception e) {
            e.printStackTrace();
            channel.txRollback();
        }
        basicConsume();
    }

    @Test
    public void confirm() throws IOException, InterruptedException {
        channel.confirmSelect();
        channel.basicPublish(myExchange, routeKey, null, messageBodyBytes);
        if (channel.waitForConfirms()) {
            System.out.println("消息发送成功" );
        }
        basicConsume();

        int num = 10;
        for (int i = 0; i < num; i++) {
            channel.basicPublish(myExchange, routeKey, null, messageBodyBytes);
        }
        channel.waitForConfirmsOrDie(); //直到所有信息都发布，只要有一个未确认就会IOException
        System.out.println("消息全部发送成功");
        for (int i = 0; i < num; i++) {
            basicConsume();
        }

        for (int i = 0; i < num; i++) {
            channel.basicPublish(myExchange, routeKey, null, messageBodyBytes);
        }
        //异步监听确认和未确认的消息
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("未确认消息，标识：" + deliveryTag);
            }
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("已确认消息，标识：%d，多个消息：%b", deliveryTag, multiple));
            }
        });
        for (int i = 0; i < num; i++) {
            basicConsume();
        }
    }

    @After
    public void after() throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    private void basicConsume() throws IOException {
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
}
