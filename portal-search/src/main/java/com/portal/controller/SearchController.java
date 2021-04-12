package com.portal.controller;

import com.portal.entity.ESGoods;
import com.portal.entity.goods.WxbGoods;
import com.portal.service.ISearchService;
import com.portal.vo.PageResultVO;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>title: com.portal.controller</p>
 * author zhuximing
 * description:
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private ISearchService iSearchService;


    @RequestMapping("init")
    public Result init(){

        return iSearchService.initData2es();
    }


    @RequestMapping("search")
    public PageResultVO<ESGoods> search(@RequestBody Map searchMap){

        return iSearchService.search(searchMap);
    }

    @RequestMapping("goods2es")
    public Result  goods2es(@RequestBody WxbGoods goods){

        return iSearchService.goods2es(goods);
    }


}