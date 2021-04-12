package com.qf;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

/**
 * <p>title: com.qf</p>
 * author zhuximing
 * description:
 */
public class Producer {
    public static void main(String[] args) throws  Exception {
        //发送消息的步骤
        //1.创建消息生产者producer(DefaultMQProducer)，并指定生产者group名称
        DefaultMQProducer producer = new DefaultMQProducer("group");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("127.0.0.1:9876");
        //3.启动producer
        producer.start();
        //4.创建Message对象，指定主题Topic、消息体
        Message message = new Message("test-topic","test0tag","你好  MQ  单向".getBytes());
        //5.发送消息【单向消息】
        //设置mq消息发送的重试次数
        producer.setRetryTimesWhenSendFailed(5);
        producer.sendOneway(message);
    }

}