package com.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.portal.entity.sec.SecKillOrder;
import com.portal.vo.Result;

public interface ISecKillOrderService extends IService<SecKillOrder> {


    //秒杀下单
    public Result secOrder(String dateStr,String goodsId);

}
