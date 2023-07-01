package com.portal.interceptor;

import io.seata.core.context.RootContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class XidInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

       //从请求头获取xid（全局事务id）
        String tx_xid = request.getHeader("TX_XID");
        if (tx_xid != null) {
                System.out.println(tx_xid);
                //将xid绑定到threadlocal
                RootContext.bind(tx_xid);
        }
        return true;
    }
}