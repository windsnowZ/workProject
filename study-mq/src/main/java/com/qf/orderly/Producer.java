package com.qf.orderly;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;

import java.util.List;

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
        for (int i = 0; i < 100; i++) {
            //4.构造消息对象
            Message message = new Message("test-topic3", "test-tag3",("message" + i).getBytes());
            //5.发送消息
            SendResult send = producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object index) {

                  int num = (int) index;

                  if(num%2==0){//偶数
                      return  list.get(0);

                  }else{
                      return list.get(1);
                  }

                }
            },i);
            System.out.println(send);
        }
        producer.shutdown();
    }
}