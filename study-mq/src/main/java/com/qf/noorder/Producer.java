package com.qf.noorder;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

/**
 * <p>title: com.qf.noorder</p>
 * author zhuximing
 * description:
 */
public class Producer {
    public static void main(String[] args) throws Exception {

        //1.创建消息生产者DefaultMQProducer，并制定生产者group
        DefaultMQProducer producer = new DefaultMQProducer("producer-group-3");

        //2.指定Nameserver地址
        producer.setNamesrvAddr("localhost:9876");
        //3.启动producer
        producer.start();
            //4.构造消息对象
        Message message = new Message("test-topic3", "test-tag3",("message -retry").getBytes());
            //5.发送消息
        SendResult send = producer.send(message);
         System.out.println(send);
        producer.shutdown();
    }
}