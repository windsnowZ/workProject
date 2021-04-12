package com.portal.service;

import com.portal.vo.Result;

/**
 * <p>title: com.portal.service</p>
 * author zhuximing
 * description:
 */
public interface IPayService {

    public Result pay(String orderId);


    //获取订单状态
    public Result status(String orderId)throws  Exception;


    //关闭订单
    public Result close(String orderId) throws Exception;
}
