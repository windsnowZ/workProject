package com.qf.noorder;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * <p>title: com.qf.noorder</p>
 * author zhuximing
 * description:
 */
public class Consumer {
    public static void main(String[] args) throws  Exception{
        //1.创建消费者Consumer(DefaultMQPushConsumer)，指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group3");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("localhost:9876");
        //3.订阅(subscribe)主题Topic
        consumer.subscribe("test-topic3","test-tag3");
        //4.设置消费模式(MessageModel),默认负载均衡模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //5.注册（register）回调函数，处理消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                try {

                    int i = 1/0;

                    for (MessageExt messageExt : list) {
                        //获取消息的topic
                        String topic = messageExt.getTopic();
                        //获取消息的标签
                        String tags = messageExt.getTags();
                        //获取消息内容
                        String content = new String(messageExt.getBody());
                        System.out.println("topic"+topic+"==tags"+tags+"==content"+content);
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e){
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }




            }
        });
        //6.启动消费者consumer
        consumer.start();
    }
}