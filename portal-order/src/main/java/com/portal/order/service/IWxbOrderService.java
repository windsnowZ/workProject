package com.portal.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.portal.dto.OrderDTO;
import com.portal.entity.order.WxbOrder;
import com.portal.vo.Result;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhuxm
 * @since 2021-04-10
 */
public interface IWxbOrderService extends IService<WxbOrder> {


        public Result order(OrderDTO orderDTO);

        public Result releaseOrder(String orderId);

}
