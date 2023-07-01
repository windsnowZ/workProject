package com.portal.interceptor;

import com.portal.JsonUtils;
import com.portal.entity.user.WxbMemeber;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>title: com.portal.interceptor</p>
 * author zhuximing
 * description:
 */
@Component
public class UserInfoInterceptor implements HandlerInterceptor {

    private static  ThreadLocal<WxbMemeber> userInfo = new ThreadLocal();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //获取用户信息
        String token = request.getHeader("token");
        if (token != null) {
            WxbMemeber wxbMemeber = JsonUtils.toBean(token, WxbMemeber.class);
            //放入到threadLocal中
            userInfo.set(wxbMemeber);
        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        userInfo.remove();
    }
    public static WxbMemeber getUserInfo(){
        return  userInfo.get();
    }

}