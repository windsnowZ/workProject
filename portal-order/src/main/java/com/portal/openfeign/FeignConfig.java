package com.portal.openfeign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.seata.core.context.RootContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


/**
 * Feign配置
 * 使用FeignClient进行服务间调用，统一传递headers信息
 */
@Configuration
public class FeignConfig implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //获取全局事务id
        String xid = RootContext.getXID();
        System.out.println(xid);
        if(!StringUtils.isEmpty(xid)) {
            //构建请求头
            requestTemplate.header("TX_XID", xid);
        }

    }
}