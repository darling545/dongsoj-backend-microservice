package com.dongsoj.dongsojbackendjudgeservice;

import com.dongsoj.dongsojbackendjudgeservice.rabbitmq.InitRabbitMq;
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
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.dongsoj")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.dongsoj.dongsojbackendserviceclient.service"})
public class DongsojBackendJudgeServiceApplication {

    public static void main(String[] args) {
        //InitRabbitMq.doInit();
        SpringApplication.run(DongsojBackendJudgeServiceApplication.class, args);
    }

}
