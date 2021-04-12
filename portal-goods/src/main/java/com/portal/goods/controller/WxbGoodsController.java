package com.portal.goods.controller;


import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.portal.JsonUtils;
import com.portal.entity.goods.SpecInfo;
import com.portal.entity.goods.WxbGoods;
import com.portal.entity.goods.WxbGoodsSku;
import com.portal.entity.order.WxbOrderItems;
import com.portal.goods.service.IWxbGoodsService;
import com.portal.goods.service.IWxbGoodsSkuService;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-02
 */
@RestController
@RequestMapping("/goods/wxb-goods")
public class WxbGoodsController {

    @Autowired
    private IWxbGoodsService iWxbGoodsService;


    
    @RequestMapping("findListBycid")
    public List<WxbGoods> findListBycid(String cid){

        //设置分页
        IPage<WxbGoods> iPage = new Page<>(1,6);


        QueryWrapper<WxbGoods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category3_id",cid);

        IPage<WxbGoods> pageInfo = iWxbGoodsService.page(iPage, queryWrapper);

        return pageInfo.getRecords();
    }
    
    
    @RequestMapping("findAuditSpuInfo")
    public List<WxbGoods>  findAuditSpuInfo(){
    
        return  iWxbGoodsService.findGoodsSpuInfo();
    }


    @RequestMapping("audit")
    public Result audit(String spuId){


        return iWxbGoodsService.auditGoods(spuId);
    }

    @RequestMapping("findGoodsBySpuId")
    public WxbGoods  findGoodsBySpuId(String spuId){

        WxbGoods wxbGoods = iWxbGoodsService.getById(spuId);
        String spuSpecInfo = wxbGoods.getSpuSpecInfo();

        //spuSpecInfo ->list
        List<SpecInfo> list = JsonUtils.toBean(spuSpecInfo, List.class);

        wxbGoods.setSpecInfos(list);
        return wxbGoods;
    }


    @Autowired
    private IWxbGoodsSkuService iWxbGoodsSkuService;


    @RequestMapping("findSkuListBySpuId")
    public List<WxbGoodsSku> findSkuListBySpuId (String spuId){

        QueryWrapper<WxbGoodsSku> skuQueryWrapper = new QueryWrapper<>();
        skuQueryWrapper.eq("goods_id",spuId);
        skuQueryWrapper.orderByDesc("is_default");
        return iWxbGoodsSkuService.list(skuQueryWrapper);
    }

    @RequestMapping("findSkuBySkuId")
    public WxbGoodsSku findSkuBySkuId(String skuId){

        return iWxbGoodsSkuService.getById(skuId);
    }
    
    //扣库存
    @RequestMapping("kkc")
    public Result  kkc(String skuId, Integer buyNum){


        //获取sku信息

        WxbGoodsSku skuInfo = iWxbGoodsSkuService.getById(skuId);

        WxbGoodsSku sku = new WxbGoodsSku();
        sku.setId(Long.parseLong(skuId));

        if (buyNum > skuInfo.getNum()) {
            return new Result(false,"库存不足");
        }


        sku.setNum(skuInfo.getNum() - buyNum);


        boolean b = iWxbGoodsSkuService.updateById(sku);
        return new Result(b,b?"success":"fail");
    }


    @RequestMapping("tkc")
    public Result tkc(@RequestBody List<WxbOrderItems> items){

        return iWxbGoodsService.tkc(items);
    }


}
