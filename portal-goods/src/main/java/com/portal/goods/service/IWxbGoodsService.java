package com.portal.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.order.WxbOrderItems;
import com.portal.vo.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-02
 */
public interface IWxbGoodsService extends IService<WxbGoods> {

    List<WxbGoods> findGoodsSpuInfo();

    Result auditGoods(String spuId);

    Result tkc(List<WxbOrderItems> items);

}
