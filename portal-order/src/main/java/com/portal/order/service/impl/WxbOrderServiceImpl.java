package com.portal.order.service.impl;

import com.api.goods.GoodsClient;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.portal.IdWorker;
import com.portal.JsonUtils;
import com.portal.dto.OrderDTO;
import com.portal.entity.address.WxbAddress;
import com.portal.entity.cart.Cart;
import com.portal.entity.order.WxbOrder;
import com.portal.entity.order.WxbOrderItems;
import com.portal.entity.user.WxbMemeber;
import com.portal.interceptor.UserInfoInterceptor;
import com.portal.order.mapper.WxbOrderItemsMapper;
import com.portal.order.mapper.WxbOrderMapper;
import com.portal.order.service.IWxbOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.portal.vo.Result;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-10
 */
@Service
public class WxbOrderServiceImpl extends ServiceImpl<WxbOrderMapper, WxbOrder> implements IWxbOrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WxbOrderItemsMapper wxbOrderItemsMapper;


    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    private IdWorker idWorker;


    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${topic}")
    private String TOPIC;
    @Value("${tag}")
    private String TAG;



    @Override
    @GlobalTransactional
    public Result order(OrderDTO orderDTO) {

        if (orderDTO == null) {
            return  new Result(false,"fail");
        }

        WxbAddress sendAddress = orderDTO.getSendAddress();

        //订单id
        String orderId = idWorker.nextId()+"";



        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();


        //获取购物车列表

        List<Object> carts = stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId()).values();


        //1:订单主表信息
        WxbOrder order = new WxbOrder();
        order.setBuyerName(sendAddress.getLocName());
        order.setSenderType(orderDTO.getSenderType());
        order.setPayType(orderDTO.getPayType());
        order.setState(0);


        order.setOrderTime(new Date());
        order.setUpdateTime(new Date());


        int buyNum = 0;

        for (Object json : carts) {
            Cart cart = JsonUtils.toBean((String) json, Cart.class);
            if(cart.isChoose()){
                buyNum += cart.getBuyNum();
            }
        }

        order.setBuyNum(buyNum);

        order.setUserId(userInfo.getMemeberId());

        order.setOrderId(orderId);

        //保存
        this.baseMapper.insert(order);


        //2:订单明细表

        for (Object json : carts) {
            Cart cart = JsonUtils.toBean((String) json, Cart.class);
            if(cart.isChoose()){
                WxbOrderItems item = new WxbOrderItems();
                item.setBuyNum(cart.getBuyNum());
                item.setSkuId(cart.getSkuId());
                item.setCreateTime(new Date());
                item.setOrderId(orderId);
                item.setSkuPrice(cart.getPrice().toString());

                //保存
                wxbOrderItemsMapper.insert(item);

                //3:扣库存【远程调用】
                goodsClient.kkc(cart.getSkuId(),cart.getBuyNum());


                //4:删除购物车
                stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId())
                    .delete(cart.getSkuId());


            }
        }


//        int i = 1/0;

//        //延时消息在下单操作时利用延时队列解决超时未支付的功能
//        rocketMQTemplate.syncSend(TOPIC+":"+TAG,
//            MessageBuilder.withPayload(orderId).build(),2000,4);



        return new Result(true,"success",orderId);
    }



    @Override
    public Result releaseOrder(String orderId) {

        //修改订单状态
        WxbOrder order = new WxbOrder();
        order.setOrderId(orderId);
        order.setState(3); //状态 0：未支付 1：已支付 2：已关闭，3：超时未支付
        order.setUpdateTime(new Date());
        this.baseMapper.updateById(order);


        //获取订单明细
        QueryWrapper<WxbOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        List<WxbOrderItems> items = wxbOrderItemsMapper.selectList(queryWrapper);


        //释放库存
        goodsClient.tkc(items);


        return new Result(true,"suucess");
    }
}
