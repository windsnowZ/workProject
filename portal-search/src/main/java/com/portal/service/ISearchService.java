package com.portal.service;

import com.portal.entity.ESGoods;
import com.portal.entity.goods.WxbGoods;
import com.portal.vo.PageResultVO;
import com.portal.vo.Result;

import java.util.Map;

/**
 * <p>title: com.portal.service</p>
 * author zhuximing
 * description:
 */
public interface ISearchService {


    public  Result initData2es();


    //商品的站内搜索

    public PageResultVO<ESGoods> search(Map searchMap);


    //同步索引库
    public Result goods2es(WxbGoods goods);

}
