package com.portal.controller;

import com.portal.entity.cart.Cart;
import com.portal.service.ICartService;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * <p>title: com.portal.controller</p>
 * author zhuximing
 * description:
 */
@RestController
@RequestMapping("cart")
public class CartController {

    @Autowired
    private ICartService cartService;
//  保存购物车
    @RequestMapping("add")
    public Result add(@RequestBody Map buyInfo){
    
        return cartService.saveCart(buyInfo);
    }

// 获取购物车列表
    @RequestMapping("list")
    public List<Cart> list(){

        return cartService.cartList();
    }

//   编辑购物车
    @RequestMapping("cartEdit")
    public Result cartEdit(@RequestBody Cart cart){

        return  cartService.cartEdit(cart);
    }

//    选择购物车信息
    @RequestMapping("listChooseCart")
    public List<Cart> listChooseCart(){

        return cartService.listChooseCart();
    }
}