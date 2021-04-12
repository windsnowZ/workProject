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

    @RequestMapping("add")
    public Result add(@RequestBody Map buyInfo){
    
        return cartService.saveCart(buyInfo);
    }


    @RequestMapping("list")
    public List<Cart> list(){

        return cartService.cartList();
    }

    @RequestMapping("cartEdit")
    public Result cartEdit(@RequestBody Cart cart){

        return  cartService.cartEdit(cart);
    }

    @RequestMapping("listChooseCart")
    public List<Cart> listChooseCart(){

        return cartService.listChooseCart();
    }
}