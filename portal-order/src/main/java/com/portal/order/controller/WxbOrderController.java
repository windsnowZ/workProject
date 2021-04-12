package com.portal.order.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.portal.dto.OrderDTO;
import com.portal.entity.order.WxbOrder;
import com.portal.entity.order.WxbOrderItems;
import com.portal.order.service.IWxbOrderItemsService;
import com.portal.order.service.IWxbOrderService;
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
 * @since 2021-04-10
 */
@RestController
@RequestMapping("/order/wxb-order")
public class WxbOrderController {

    @Autowired
    private IWxbOrderService wxbOrderService;

    @Autowired
    private IWxbOrderItemsService wxbOrderItemsService;



    @RequestMapping("save")
    public Result save(@RequestBody OrderDTO dto){
    
        return wxbOrderService.order(dto);
    }



    @RequestMapping("findOrderById")
    public WxbOrder findOrderById(String orderId){

        WxbOrder wxbOrder = wxbOrderService.getById(orderId);

        //获取订单明细信息
        QueryWrapper<WxbOrderItems> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",orderId);
        List<WxbOrderItems> items = wxbOrderItemsService.list(queryWrapper);


        wxbOrder.setItems(items);


        return wxbOrder;
    }

    @RequestMapping("update")
    public Result update(@RequestBody WxbOrder order){


        boolean b = wxbOrderService.updateById(order);


        return new Result(b,b?"success":"fail");
    }
    
    @RequestMapping("releaseOrder")
    public Result releaseOrder(String orderId){
    
        return wxbOrderService.releaseOrder(orderId);
    }


}
