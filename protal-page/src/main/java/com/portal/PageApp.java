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
@EnableFeignClients(basePackages = {"com.api.goods"})
@EnableDiscoveryClient
public class PageApp {

    public static void main(String[] args) {
        SpringApplication.run(PageApp.class,args);
    }

}