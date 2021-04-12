package com.portal.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.portal.entity.goods.WxbGoods;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-02
 */
public interface WxbGoodsMapper extends BaseMapper<WxbGoods> {

    List<WxbGoods> findGoodsSpuInfo();


    WxbGoods findGoodsById(String spuId);

}
