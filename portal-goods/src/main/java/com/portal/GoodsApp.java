package com.portal;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.api.es","com.api.cms"})
@MapperScan("com.portal.goods.mapper")
public class GoodsApp {

    public static void main(String[] args) {
        SpringApplication.run(GoodsApp.class,args);
    }
}