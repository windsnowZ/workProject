package com.qf.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>title: com.mq</p>
 * author zhuximing
 * description:
 */
@RestController
public class IndexController {


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @RequestMapping("syncSend")
    public SendResult syncSend() {
        // 同步发送消息
        return rocketMQTemplate.syncSend("test-topic8:test-tag8","你好mq");
    }

    @RequestMapping("asyncSend")
    public void asyncSend() {

        // 异步发送消息
        rocketMQTemplate.asyncSend("test-topic8:test-tag8", "asyncSend", new SendCallback() {
            public void onSuccess(SendResult sendResult) {
                System.out.println(sendResult);
            }

            public void onException(Throwable throwable) {
                  throwable.printStackTrace();
            }
        });
    }

    @RequestMapping("onewaySend")
    public void onewaySend() {
        // oneway 发送消息
        rocketMQTemplate.sendOneWay("test-topic8:test-tag8", "oneway");
    }

    @GetMapping("delay")
    public String delay(){
        //延时消息
        rocketMQTemplate.syncSend("test-topic8:test-tag8", MessageBuilder.withPayload("delay").build(),2000,2);

        return "delay";
    }


}