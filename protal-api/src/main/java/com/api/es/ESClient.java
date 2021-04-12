package com.api.es;

import com.portal.entity.goods.WxbGoods;
import com.portal.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>title: com.api</p>
 * author zhuximing
 * description:
 */
@Component
@FeignClient("portal-search")
@RequestMapping("/search")
public interface ESClient {
    @RequestMapping("goods2es")
    public Result goods2es(@RequestBody WxbGoods goods);
}
