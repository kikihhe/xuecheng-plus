package com.xuecheng;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@EnableSwagger2Doc
@EnableHystrix
@SpringBootApplication
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
public class ContentApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentApiApplication.class, args);
    }

}
