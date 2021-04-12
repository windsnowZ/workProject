package com.portal.listener;

import com.api.goods.GoodsClient;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.goods.WxbGoodsSku;
import freemarker.template.Configuration;
import freemarker.template.Template;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RocketMQMessageListener(consumerGroup = "portal-page-consumer",
    topic = "goods-2-page",selectorExpression = "goods-2-page-tag",
    consumeMode = ConsumeMode.CONCURRENTLY,
    messageModel = MessageModel.BROADCASTING)
public class ConsumerListener implements RocketMQListener<String> {


    @Autowired
    private FreeMarkerConfig freeMarkerConfig;
    
    @Value("${html.sink}")
    private String SINK;

    @Autowired
    private GoodsClient goodsClient;


    public void onMessage(String spuId) {


        System.out.println("spuId:"+spuId);

        try {
            //生成静态化页面
            genHTML(spuId);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    private void  genHTML(String spuId) throws Exception{
        Configuration configuration = freeMarkerConfig.getConfiguration();

        Template template = configuration.getTemplate("introduction.ftl");

        //获取规格信息
        WxbGoods goods = goodsClient.findGoodsBySpuId(spuId);

        //根据spuid获取sku的列表
        List<WxbGoodsSku> skuList = goodsClient.findSkuListBySpuId(spuId);


        Map data = new HashMap();
        data.put("specs",goods.getSpecInfos());
        data.put("skuList",skuList);
        Writer out = new FileWriter(SINK+"/"+spuId+".html");
        template.process(data,out);
        out.close();


    }
}