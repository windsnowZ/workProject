package com.portal.cms.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.portal.cms.service.IContentCatService;
import com.portal.cms.service.IContentService;
import com.portal.entity.cms.Content;
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
 * @since 2021-04-02
 */
@RestController
@RequestMapping("/cms/content")
public class ContentController {

    @Autowired
    private IContentService iContentService;


    @RequestMapping("findContetByCid")
    public List<Content> findContetByCid(String cid){


        QueryWrapper<Content> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_id",cid);

        return  iContentService.list(queryWrapper);
    }

}
