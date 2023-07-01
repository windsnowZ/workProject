package com.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * <p>title: com.portal</p>
 * author zhuximing
 * description:
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.portal.mapper")
public class SecApp {

    public static void main(String[] args) {
        SpringApplication.run(SecApp.class,args);
    }


    @Bean
    public IdWorker getIdWorker(){
        return  new IdWorker();
    }


    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
//        config.useClusterServers()
//            .setScanInterval(2000)
//            .addNodeAddress("redis://10.0.29.30:6379", "redis://10.0.29.95:6379")
//            .addNodeAddress("redis://10.0.29.205:6379");

        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
//              .setPassword().setDatabase()

        RedissonClient redisson = Redisson.create(config);

        return redisson;
    }
}