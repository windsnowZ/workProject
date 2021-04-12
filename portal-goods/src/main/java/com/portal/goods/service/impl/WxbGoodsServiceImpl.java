package com.portal.goods.service.impl;

import com.api.es.ESClient;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.goods.WxbGoodsSku;
import com.portal.entity.order.WxbOrderItems;
import com.portal.goods.mapper.WxbGoodsMapper;
import com.portal.goods.mapper.WxbGoodsSkuMapper;
import com.portal.goods.service.IWxbGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.portal.vo.Result;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-02
 */
@Service
@Transactional
public class WxbGoodsServiceImpl extends ServiceImpl<WxbGoodsMapper, WxbGoods> implements IWxbGoodsService {


    @Autowired
    private ESClient esClient;

    @Autowired
    private WxbGoodsSkuMapper wxbGoodsSkuMapper;


    @Override
    public List<WxbGoods> findGoodsSpuInfo() {

        return this.baseMapper.findGoodsSpuInfo();
    }

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${topic.es}")
    private String ESTOPIC;
    @Value("${tag.es}")
    private String ESTAG;

    @Value("${topic.page}")
    private String PAGETOPIC;
    @Value("${tag.page}")
    private String PAGETAG;
    
    
    
    @Override
    public Result auditGoods(String spuId) {

        //1:根据spuId获取商品信息
        WxbGoods goods = this.baseMapper.findGoodsById(spuId);
        if (goods == null)
            return  new Result(false,"该商品不存在");


        //2：修改商品的审核状态
        goods.setAuditStatus("1");
        int i = this.baseMapper.updateById(goods);


        //3：同步索引库
        //esClient.goods2es(goods);

        //发送消息【同步索引库】
        SendResult sendResult = rocketMQTemplate.syncSend(ESTOPIC + ":" + ESTAG, goods);
        SendResult sendResult1 = rocketMQTemplate.syncSend(PAGETOPIC + ":" + PAGETAG, spuId);

        //生成静态化页面

        SendStatus sendStatus = sendResult.getSendStatus();
        SendStatus sendStatus1 = sendResult1.getSendStatus();
        if(sendStatus == SendStatus.SEND_OK && sendStatus1 == SendStatus.SEND_OK){
            return new Result(true,"审核成功");
        }else {
            //todo:哪个消息  什么时间  发送状态【补偿】
            return new Result(true,"同步失败");
        }


    }

    @Override
    public Result tkc(List<WxbOrderItems> items) {

        if (items == null) {
            return  new Result(false,"fail");
        }

        items.forEach(item->{

            //获取sku
            WxbGoodsSku sku = wxbGoodsSkuMapper.selectById(item.getSkuId());

            UpdateWrapper<WxbGoodsSku> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("num",sku.getNum()+item.getBuyNum())
                .eq("id",item.getSkuId());

            wxbGoodsSkuMapper.update(null,updateWrapper);


        });



        return new Result(true,"success");
    }
}
