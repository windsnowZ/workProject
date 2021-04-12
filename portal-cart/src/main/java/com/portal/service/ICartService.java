package com.portal.service;

import com.portal.entity.cart.Cart;
import com.portal.vo.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>title: com.portal.service</p>
 * author zhuximing
 * description:
 */
public interface ICartService {


    //加购
    public Result saveCart(Map buyInfo);

    //购物车列表
    public List<Cart> cartList();

    //修改购物车
    public  Result cartEdit(Cart cart);

    //获取用户勾选的购物车列表

    public  List<Cart> listChooseCart();

}
