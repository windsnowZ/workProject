package com.portal;

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
@EnableFeignClients(basePackages = {"com.api.goods"})
public class CartApp {
    public static void main(String[] args) {
        SpringApplication.run(CartApp.class,args);
    }
}