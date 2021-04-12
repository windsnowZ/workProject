package com.portal.uc.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.portal.entity.user.WxbMemeber;
import com.portal.interceptor.UserInfoInterceptor;
import com.portal.uc.entity.WxbAddress;
import com.portal.uc.service.IWxbAddressService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/uc/wxb-address")
public class WxbAddressController {


    @Autowired
    private IWxbAddressService wxbAddressService;

    @RequestMapping("list")
    public List<WxbAddress> list(){

        //获取用户信息
        WxbMemeber userInfo = UserInfoInterceptor.getUserInfo();
        QueryWrapper<WxbAddress> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("loc_member_id",userInfo.getMemeberId());


        return wxbAddressService.list(queryWrapper);
    }
    
    
}
