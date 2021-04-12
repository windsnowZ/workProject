package com.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.portal.user.mapper")
public class SSOApp {

    public static void main(String[] args) {
        SpringApplication.run(SSOApp.class,args);
    }

}