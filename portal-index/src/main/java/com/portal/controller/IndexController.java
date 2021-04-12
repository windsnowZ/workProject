package com.portal.controller;

import com.portal.entity.cms.Content;
import com.portal.entity.goods.WxbGoods;
import com.portal.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>title: com.portal.controller</p>
 * author zhuximing
 * description:
 */
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private IndexService indexService;


    //根据cid获取内容列表
    @RequestMapping("findContetByCid")
    public List<Content> findContetByCid(String cid){

        return indexService.listContentByCid(cid);
    }


    //根据商品分类id获取热门商品列表

    @RequestMapping("/hot/goodsList")
    public List<WxbGoods>  goodsList(String cid){

        return indexService.listHotGoodsByCid(cid);
    }

}