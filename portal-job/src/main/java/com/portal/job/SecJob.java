package com.portal.job;

import com.api.sec.SecClient;
import com.portal.DateUtil;
import com.portal.JsonUtils;
import com.portal.entity.sec.SecKillGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * <p>title: com.portal.job</p>
 * author zhuximing
 * description:
 */
@Component
public class SecJob {

    @Autowired
    private SecClient secClient;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Scheduled(cron="0/5 * * * * ?")
    public void test(){
        //System.out.println(new Date());


        //1.获取时间区间列表，一共5个时间区间（正在开始以及即将开始）
        List<Date> dateMenus = DateUtil.getDateMenus();

        //2.遍历时间区间获取时间区间的秒杀商品信息
        for (Date dateMenu : dateMenus) {


            String startTime = DateUtil.date2Str(dateMenu);

            List<SecKillGoods> secKillGoods = secClient.findSecByDate(startTime);

            //3.放入redis

            BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("sec:" + startTime);


            secKillGoods.forEach(goods->{
                operations.put(goods.getSeckillId()+"", JsonUtils.toString(goods));
            });

        }


        //3:将上一个活动时间的数据同步到mysql

    }




}