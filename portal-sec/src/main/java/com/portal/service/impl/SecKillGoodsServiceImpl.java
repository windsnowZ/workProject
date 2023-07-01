package com.portal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.portal.DateUtil;
import com.portal.entity.sec.SecKillGoods;
import com.portal.mapper.SecKillGoodsMapper;
import com.portal.service.ISecKillGoodsService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class SecKillGoodsServiceImpl extends ServiceImpl<SecKillGoodsMapper, SecKillGoods> implements ISecKillGoodsService {


    //2021042116   //2021042118
    @Override
    public List<SecKillGoods> findSecByDate(String date) {

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHH");
            Date startTime = format.parse(date);


            Date endTime = DateUtil.addDateHour(startTime, 2);

            QueryWrapper<SecKillGoods> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("seckill_status",1);
            queryWrapper.gt("seckill_count",0);
            queryWrapper.ge("seckill_starttime",startTime);
            queryWrapper.le("seckill_endtime",endTime);

            List<SecKillGoods> secKillGoods = this.baseMapper.selectList(queryWrapper);


            //要求：1.审核状态必须是seckill_status=1
            //     2.商品秒杀库存数必须seckill_count>0
            //     3.开始时间>=时间区间的开始时间、结束时间<=时间区间开始时间+2小时

            return secKillGoods;
        } catch (ParseException e) {
            e.printStackTrace();
            return  null ;
        }

    }
}