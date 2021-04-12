package com.api.cms;

import com.portal.entity.cms.Content;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>title: com.api</p>
 * author zhuximing
 * description:
 */
@Component
@FeignClient("portal-cms")
@RequestMapping("/cms/content")
public interface CmsClient {
    @RequestMapping("findContetByCid")
    public List<Content> findContetByCid(@RequestParam("cid") String cid);
}
