package com.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.portal.cms.mapper")
public class CMSApp {

    public static void main(String[] args) {
        SpringApplication.run(CMSApp.class,args);
    }

}