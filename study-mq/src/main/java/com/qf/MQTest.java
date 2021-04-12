package com.qf;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * <p>title: com.qf</p>
 * author zhuximing
 * description:
 */
public class MQTest {
    public static void main(String[] args) throws  Exception {
        //1.创建消费者Consumer(DefaultMQPushConsumer)，指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //3.订阅(subscribe)主题Topic
        consumer.subscribe("test-topic","test0tag");
        //4.设置消费模式(MessageModel),默认集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //5.注册（register）回调函数，处理消息
        consumer.setPullBatchSize(3);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                try {
                    //模拟业务
                    list.forEach(msg->{

                        String topic = msg.getTopic();
                        String tags = msg.getTags();
                        String content = new String(msg.getBody());
                        System.out.println("topic=="+topic);
                        System.out.println("tags=="+tags);
                        System.out.println("content=="+content);

                    });
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception ex){
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }

            }
        });
        //6.启动消费者
        consumer.start();
    }
}