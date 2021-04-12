package com.portal.listener;

import com.api.goods.GoodsClient;
import com.api.order.OrderClient;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.goods.WxbGoodsSku;
import com.portal.service.IPayService;
import com.portal.vo.Result;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RocketMQMessageListener(consumerGroup = "portal-pay-consumer",
    topic = "order",selectorExpression = "order-delay",
    consumeMode = ConsumeMode.CONCURRENTLY,
    messageModel = MessageModel.CLUSTERING)
public class ConsumerListener implements RocketMQListener<String> {


    @Autowired
    private IPayService payService;

    @Autowired
    private OrderClient orderClient;

    @Override
    public void onMessage(String orderId) {

        System.out.println("!!!!!!!"+orderId);

        try {
            //判断订单的支付状态
            Result result = payService.status(orderId);

            System.out.println("result"+result);

            if("NOTPAY".equals(result.getData())){//超时未支付

               //关闭订单
                Result closeRes = payService.close(orderId);
                System.out.println("closeRes"+closeRes);
                if(closeRes.isSuccess()){
                    //释放库存【远程调用】
                    orderClient.releaseOrder(orderId);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}