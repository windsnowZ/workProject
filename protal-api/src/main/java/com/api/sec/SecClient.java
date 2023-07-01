package com.api.sec;

import com.portal.entity.sec.SecKillGoods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>title: com.api.sec</p>
 * author zhuximing
 * description:
 */
@Component
@FeignClient("portal-sec")
@RequestMapping("sec")
public interface SecClient {
    @RequestMapping("findSecByDate")
    public List<SecKillGoods> findSecByDate(@RequestParam("date") String date);
}
