package com.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.api.goods"})
@EnableDiscoveryClient
@MapperScan("com.portal.order.mapper")
public class OrderApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderApp.class,args);
    }



    @Bean
    public IdWorker getIdWorker(){
        return new IdWorker(1,1);
    }
}