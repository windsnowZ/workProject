package com.portal.service.impl;

import com.api.goods.GoodsClient;
import com.portal.JsonUtils;
import com.portal.entity.cart.Cart;
import com.portal.entity.goods.WxbGoodsSku;
import com.portal.entity.user.WxbMemeber;
import com.portal.interceptor.UserInfoInterceptor;
import com.portal.service.ICartService;
import com.portal.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>title: com.portal.service.impl</p>
 * author zhuximing
 * description:
 */
@Service
public class CartService implements ICartService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private GoodsClient goodsClient;

    /*传递skuid和购买的数量*/

    @Override
    public Result saveCart(Map buyInfo) {

        if (buyInfo == null) {
            return  new Result(false,"fail");
        }



        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();
        System.out.println(userInfo);

        //获取用户的购物车
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId());

//        大key是Cart+用户信息

        //获取sku
        Object cartJson = operations.get(buyInfo.get("skuId"));

//        小key是skuid

//        object是订单信息

//        如果订单不存在的情况

        if (cartJson == null) {//要买的sku在购物车中不存在

            //远程调用protal-goods 获取sku信息
            WxbGoodsSku sku = goodsClient.findSkuBySkuId((String) buyInfo.get("skuId"));


            //加购
            Cart cart = new Cart();
            cart.setBuyNum((Integer) buyInfo.get("buyNum"));
            cart.setSkuId((String) buyInfo.get("skuId"));
            cart.setImg(sku.getImage());
            cart.setMarketPrice(sku.getMarketPrice().doubleValue());
            cart.setPrice(sku.getPrice().doubleValue());
            cart.setSellerId(sku.getSellerId());
            cart.setSellerName(sku.getSeller());
            cart.setSpec(sku.getSpec());
            cart.setTitle(sku.getTitle());


            //保存redis
            operations.put(buyInfo.get("skuId"), JsonUtils.toString(cart));


        }else{//存在

            //修改数量
            Cart cart = JsonUtils.toBean((String) cartJson, Cart.class);

            cart.setBuyNum(cart.getBuyNum()+(Integer) buyInfo.get("buyNum") );

            //覆盖到redis
            operations.put(buyInfo.get("skuId"),JsonUtils.toString(cart));

        }



        return new Result(true,"success");
    }

    @Override
    public List<Cart> cartList() {

        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();

        //从redis中获取购物车列表
        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId());

        List<Object> values = operations.values();

        List myCarts = new ArrayList();


//        获取用户的订单信息
        values.forEach(json->{
            Cart cart = JsonUtils.toBean((String) json, Cart.class);
            myCarts.add(cart);
        });

        return myCarts;
    }

    @Override
    public Result cartEdit(Cart cart) {
        if (cart == null) {
            return new Result(false,"fail");
        }
        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();

        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId());

        //覆盖
//      编辑购物车信息
        operations.put(cart.getSkuId(),JsonUtils.toString(cart));


        return new Result(true,"success");
    }

    @Override
    public List<Cart> listChooseCart() {
        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();

        BoundHashOperations<String, Object, Object> operations = stringRedisTemplate.boundHashOps("CART:" + userInfo.getMemeberId());

        List<Object> cartJsons = operations.values();

        List<Cart> carts = new ArrayList<>();
//        选择购物车信息
        cartJsons.forEach(json->{
            Cart cart = JsonUtils.toBean((String) json, Cart.class);
            if(cart.isChoose()){
                carts.add(cart);
            }
        });


        return carts;
    }
}