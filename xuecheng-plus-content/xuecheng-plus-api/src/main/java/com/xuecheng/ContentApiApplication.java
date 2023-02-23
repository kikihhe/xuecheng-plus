package com.xuecheng;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger2Doc
@EnableHystrix
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
public class ContentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApiApplication.class, args);
    }

}
