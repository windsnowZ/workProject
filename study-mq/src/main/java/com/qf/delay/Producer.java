package com.qf.delay;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * <p>title: com.qf.delay</p>
 * author zhuximing
 * description:
 */
public class Producer {
    public static void main(String[] args) throws  Exception {
        //1.创建消息生产者producer(DefaultMQProducer)，并制定生产者group
        DefaultMQProducer producer = new DefaultMQProducer("producer-group-5");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();
        //4.创建Message对象，指定主题Topic、消息体
        Message message = new Message("test-topic5", "test-tag5"/*用于消息过滤*/, "你好 mq".getBytes());


        //messageDelayLevel=5s 10s 15s 35s 25s 35s 55s 120s 5s 5s 5s 5s 5s 5s 5s 5s 5s 5s
        message.setDelayTimeLevel(4);


        //5.发送消息
        SendResult send = producer.send(message);
        System.out.println(send);
        //6.关闭生产者producer
        producer.shutdown();
    }
}