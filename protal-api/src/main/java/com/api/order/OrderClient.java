package com.api.order;

import com.portal.entity.order.WxbOrder;
import com.portal.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>title: com.api.order</p>
 * author zhuximing
 * description:
 */
@Component
@FeignClient("portal-order")
@RequestMapping("/order/wxb-order")
public interface OrderClient {

    @RequestMapping("findOrderById")
    public WxbOrder findOrderById(@RequestParam("orderId") String orderId);


    @RequestMapping("update")
    public Result update(@RequestBody WxbOrder order);



    @RequestMapping("releaseOrder")
    public Result releaseOrder(@RequestParam("orderId") String orderId);
}
