package com.dongsoj.dongsojbackendquestionservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication()
@MapperScan("com.dongsoj.dongsojbackendquestionservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.dongsoj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.dongsoj.dongsojbackendserviceclient.service"})
public class DongsojBackendQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongsojBackendQuestionServiceApplication.class, args);
    }

}
