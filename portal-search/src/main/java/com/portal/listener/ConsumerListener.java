package com.portal.listener;

import com.portal.entity.goods.WxbGoods;
import com.portal.service.ISearchService;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(consumerGroup = "portal-search-consumer",
    topic = "goods-2-es",selectorExpression = "goods-2-es-tag",
    consumeMode = ConsumeMode.CONCURRENTLY,
    messageModel = MessageModel.CLUSTERING)
public class ConsumerListener implements RocketMQListener<WxbGoods> {

    @Autowired
    private ISearchService iSearchService;


    public void onMessage(WxbGoods goods) {

        //同步到es
        iSearchService.goods2es(goods);



    }
}