package com.api.goods;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.goods.WxbGoodsSku;
import com.portal.entity.order.WxbOrderItems;
import com.portal.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>title: com.api</p>
 * author zhuximing
 * description:
 */
@Component
@FeignClient("portal-goods")
@RequestMapping("/goods/wxb-goods")
public interface GoodsClient {
    @RequestMapping("findListBycid")
    public List<WxbGoods> findListBycid(@RequestParam("cid") String cid);


    @RequestMapping("findAuditSpuInfo")
    public List<WxbGoods>  findAuditSpuInfo();


    @RequestMapping("findGoodsBySpuId")
    public WxbGoods  findGoodsBySpuId(@RequestParam("spuId") String spuId);


    @RequestMapping("findSkuListBySpuId")
    public List<WxbGoodsSku> findSkuListBySpuId (@RequestParam("spuId") String spuId);

    @RequestMapping("findSkuBySkuId")
    public WxbGoodsSku findSkuBySkuId(@RequestParam("skuId") String skuId);

    @RequestMapping("kkc")
    public Result kkc(@RequestParam("skuId") String skuId, @RequestParam("buyNum") Integer buyNum);

    @RequestMapping("tkc")
    public Result tkc(@RequestBody List<WxbOrderItems> items);

}
