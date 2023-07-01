package com.portal.controller;

import com.portal.DateUtil;
import com.portal.entity.sec.SecKillGoods;
import com.portal.service.ISecKillGoodsService;
import com.portal.service.ISecKillOrderService;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * <p>title: com.portal.controller</p>
 * author zhuximing
 * description:
 */
@RestController
@RequestMapping("sec")
public class SecController {

    @Autowired
    private ISecKillGoodsService secKillGoodsService;

    @Autowired
    private ISecKillOrderService secKillOrderService;


    @RequestMapping("findSecByDate")
    public List<SecKillGoods> findSecByDate(String date){

        return secKillGoodsService.findSecByDate(date);
    }


    @RequestMapping("findDateMenues")
    public List<Date> findDateMenues(){

        return DateUtil.getDateMenus();
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //根据时间区间获取对应的商品列表    sec:2021042322
    @RequestMapping("findSecByDateBetw")
    public List<Object>  findSecByDateBetw(String date){

        List<Object> values = stringRedisTemplate.boundHashOps("sec:" + date).values();

        return values;
    }




    @RequestMapping("/gendata")
    public String gendata(){
        //获取最近的5个时间区间
        List<Date> dateMenus = DateUtil.getDateMenus();

        for (Date dateMenu : dateMenus) {
            Random random = new Random();
            int randomv = random.nextInt(200);
            //每个区间插入模拟数据
            SecKillGoods secKillGoods = new SecKillGoods();
            secKillGoods.setPrice(399.99);
            secKillGoods.setSeckillPrice(99.99);
            secKillGoods.setSeckillCount(10);
            secKillGoods.setSeckillRemaining(10);
            secKillGoods.setSeckillGoodsTitle("华为p40pro 亮黑色 8+128G全网通 立减"+randomv);
            secKillGoods.setSeckillStatus(1);
            secKillGoods.setCreatedate(new Date());
            secKillGoods.setSeckillStarttime(DateUtil.addDateMinutes(dateMenu,2));
            secKillGoods.setSeckillEndtime(DateUtil.addDateMinutes(dateMenu,60));
            secKillGoodsService.save(secKillGoods);
        }


        return "success";


    }


    @RequestMapping("order")
    public Result order(String dateStr,String goodsId){

        return secKillOrderService.secOrder(dateStr,goodsId);
    }




}