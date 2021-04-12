package com.portal.service.impl;

import com.api.cms.CmsClient;
import com.api.goods.GoodsClient;
import com.portal.JsonUtils;
import com.portal.entity.cms.Content;
import com.portal.entity.goods.WxbGoods;
import com.portal.service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>title: com.portal.service.impl</p>
 * author zhuximing
 * description:
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Autowired
    private CmsClient cmsClient;

    @Autowired
    private GoodsClient goodsClient;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<Content> listContentByCid(String cid) {

        //从redis中获取
        //key cms
                // field : catid=1   value:json

        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps("cms");

        Object contentListJson =  ops.get("catid" + cid);
        if (contentListJson == null) {//redis中没有

            //get from db
            List<Content> contetByCid = cmsClient.findContetByCid(cid);

            //save to redis
            ops.put("catid" + cid, JsonUtils.toString(contetByCid));

            return contetByCid;
        }else{
            //contentListJson -> contentList
            List<Content> list = JsonUtils.toBean((String) contentListJson, List.class);

            return  list;
        }


    }


    @Override
    public List<WxbGoods> listHotGoodsByCid(String cid) {

        //从redis中获取
        BoundHashOperations<String, Object, Object> ops = stringRedisTemplate.boundHashOps("hot:goods");

        Object goodsListJson = ops.get("catid" + cid);
        if (goodsListJson == null) {//redis中没有

            //get from db
            List<WxbGoods> goodsList = goodsClient.findListBycid(cid);


            //save to redis
            ops.put("catID"+cid,JsonUtils.toString(goodsList));

            return goodsList;

        }


        //goodsListJson -> goodsList
        List<WxbGoods> list = JsonUtils.toBean((String) goodsListJson, List.class);


        return list;
    }
}