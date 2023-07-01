package com.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.portal.IdWorker;
import com.portal.JsonUtils;
import com.portal.entity.sec.SecKillGoods;
import com.portal.entity.sec.SecKillOrder;
import com.portal.mapper.SecKillOrderMapper;
import com.portal.service.ISecKillOrderService;
import com.portal.vo.Result;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class SecKillOrderServiceImpl extends ServiceImpl<SecKillOrderMapper, SecKillOrder> implements ISecKillOrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @Autowired
    private IdWorker idWorker;


    @Autowired
    private RedissonClient redissonClient;

    @Override
//    传递秒杀的日期和商品id
    public Result secOrder(String dateStr, String goodsId) {
        //获取锁
        RLock lock = redissonClient.getLock("lock" + goodsId);
        lock.lock(30,TimeUnit.SECONDS);//加锁

        //1:从redis中获取商品的信息
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("sec:" + dateStr);

        Object goodsJsonStr = operations.get(goodsId);

        if (goodsJsonStr == null) {
            lock.unlock();

            return  new Result(false,"商品不存在");
        }

        SecKillGoods secKillGoods = JsonUtils.toBean((String) goodsJsonStr, SecKillGoods.class);

        //2:判断库存
        if(secKillGoods.getSeckillRemaining() <= 0){
            lock.unlock();
            return  new Result(false,"库存不足");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //3:生成订单
        SecKillOrder order = new SecKillOrder();
        order.setCreatedate(new Date());

//        秒杀商品的id
        order.setSeckillGoodsId(secKillGoods.getSeckillId()+"");

//订单的id
        long orderId = idWorker.nextId();

//设置秒杀的订单id
        order.setSeckillOrderId(orderId+"");

//        秒杀的用户id
        String userId = "xx"+idWorker.nextId();
        order.setSeckillUserId(userId);

//        秒杀的用户id
        order.setSeckillMoney(secKillGoods.getSeckillPrice());

        //4:保存订单 hash   sec:orders:goodsId           userid        orderInfo

        BoundHashOperations<String, Object, Object> operations1 = stringRedisTemplate.boundHashOps("sec:orders:" + secKillGoods.getSeckillId());

        operations1.put(userId,JsonUtils.toString(order));

        //5:扣库存
        secKillGoods.setSeckillRemaining(secKillGoods.getSeckillRemaining()-1);
        operations.put(goodsId,JsonUtils.toString(secKillGoods));








        try {
            lock.unlock();
            return new Result(true,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"fail");
        }


    }



//
//    @Override
//    public Result secOrder(String dateStr, String goodsId) {
//
//        //获取锁
//        Boolean hasLock = stringRedisTemplate.boundValueOps("lock"+goodsId).setIfAbsent("xx",10, TimeUnit.SECONDS);
//
//        if(hasLock){//非阻塞锁
//            //1:从redis中获取商品的信息
//            BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("sec:" + dateStr);
//
//            Object goodsJsonStr = operations.get(goodsId);
//
//            if (goodsJsonStr == null) {
//                return  new Result(false,"商品不存在");
//            }
//
//            SecKillGoods secKillGoods = JsonUtils.toBean((String) goodsJsonStr, SecKillGoods.class);
//
//            //2:判断库存
//            if(secKillGoods.getSeckillRemaining() <= 0){
//                return  new Result(false,"库存不足");
//            }
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            //3:生成订单
//            SecKillOrder order = new SecKillOrder();
//            order.setCreatedate(new Date());
//            order.setSeckillGoodsId(secKillGoods.getSeckillId()+"");
//
//            long orderId = idWorker.nextId();
//
//            order.setSeckillOrderId(orderId+"");
//            String userId = "xx"+idWorker.nextId();
//            order.setSeckillUserId(userId);
//            order.setSeckillMoney(secKillGoods.getSeckillPrice());
//
//            //4:保存订单 hash   sec:orders:goodsId           userid        orderInfo
//
//            BoundHashOperations<String, Object, Object> operations1 = stringRedisTemplate.boundHashOps("sec:orders:" + secKillGoods.getSeckillId());
//
//            operations1.put(userId,JsonUtils.toString(order));
//
//            //5:扣库存
//            secKillGoods.setSeckillRemaining(secKillGoods.getSeckillRemaining()-1);
//            operations.put(goodsId,JsonUtils.toString(secKillGoods));
//
//
//            //释放锁
//            stringRedisTemplate.delete("lock"+goodsId);
//
//
//            return new Result(true,"success");
//
//
//
//        }else{
//            return new Result(false,"系统繁忙");
//        }
//
//
//
//
//    }
//
//    @Override
//    public Result secOrder(String dateStr, String goodsId) {
//
//        //获取锁  阻塞锁
//        while (stringRedisTemplate.boundValueOps("lock"+goodsId).setIfAbsent("xx")){
//            //1:从redis中获取商品的信息
//            BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("sec:" + dateStr);
//
//            Object goodsJsonStr = operations.get(goodsId);
//
//            if (goodsJsonStr == null) {
//                return  new Result(false,"商品不存在");
//            }
//
//            SecKillGoods secKillGoods = JsonUtils.toBean((String) goodsJsonStr, SecKillGoods.class);
//
//            //2:判断库存
//            if(secKillGoods.getSeckillRemaining() <= 0){
//                return  new Result(false,"库存不足");
//            }
//
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//
//            //3:生成订单
//            SecKillOrder order = new SecKillOrder();
//            order.setCreatedate(new Date());
//            order.setSeckillGoodsId(secKillGoods.getSeckillId()+"");
//
//            long orderId = idWorker.nextId();
//
//            order.setSeckillOrderId(orderId+"");
//            String userId = "xx"+idWorker.nextId();
//            order.setSeckillUserId(userId);
//            order.setSeckillMoney(secKillGoods.getSeckillPrice());
//
//            //4:保存订单 hash   sec:orders:goodsId           userid        orderInfo
//
//            BoundHashOperations<String, Object, Object> operations1 = stringRedisTemplate.boundHashOps("sec:orders:" + secKillGoods.getSeckillId());
//
//            operations1.put(userId,JsonUtils.toString(order));
//
//            //5:扣库存
//            secKillGoods.setSeckillRemaining(secKillGoods.getSeckillRemaining()-1);
//            operations.put(goodsId,JsonUtils.toString(secKillGoods));
//            return new Result(true,"success");
//        }
//
//
//
//
//    }
}