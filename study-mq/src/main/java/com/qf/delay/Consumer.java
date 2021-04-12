package com.qf.delay;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * <p>title: com.qf.delay</p>
 * author zhuximing
 * description:
 */
public class Consumer {
    public static void main(String[] args) throws Exception {
        //1.创建消费者Consumer(DefaultMQPushConsumer)，指定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group5");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("localhost:9876");
        //3.订阅(subscribe)主题Topic
        consumer.subscribe("test-topic5","test-tag5");
        //4.设置消费模式(MessageModel),默认集群模式
        consumer.setMessageModel(MessageModel.CLUSTERING);
        //5.注册（register）回调函数，处理消息
        consumer.setConsumeMessageBatchMaxSize(2);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

                try {
                    for (MessageExt messageExt : list) {
                        //获取消息的topic
                        String topic = messageExt.getTopic();
                        //获取消息的标签
                        String tags = messageExt.getTags();
                        //获取消息内容
                        String content = new String(messageExt.getBody());
                        System.out.println("topic"+topic+"==tags"+tags+"==content"+content);
                        //打印演示消费的时间
                        System.out.println(System.currentTimeMillis() - messageExt.getStoreTimestamp());
                    }
                }catch (Exception e){
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }


                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //6.启动消费者
        consumer.start();
    }
}