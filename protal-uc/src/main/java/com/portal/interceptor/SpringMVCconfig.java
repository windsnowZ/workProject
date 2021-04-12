package com.portal.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>title: com.portal.interceptor</p>
 * author zhuximing
 * description:
 */
@Configuration
public class SpringMVCconfig implements WebMvcConfigurer {

   @Autowired
   private UserInfoInterceptor userInfoInterceptor;

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInfoInterceptor)
            .addPathPatterns("/**");
    }
}
