package com.portal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.portal.entity.sec.SecKillGoods;

import java.util.List;

public interface ISecKillGoodsService extends IService<SecKillGoods> {

    public List<SecKillGoods> findSecByDate(String date);

}
