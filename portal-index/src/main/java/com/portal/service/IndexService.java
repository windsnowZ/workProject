package com.portal.service;

import com.portal.entity.cms.Content;
import com.portal.entity.goods.WxbGoods;

import java.util.List;

/**
 * <p>title: com.portal.service</p>
 * author zhuximing
 * description:
 */
public interface IndexService {


    public List<Content> listContentByCid(String cid);


    public List<WxbGoods> listHotGoodsByCid(String cid);

}
