package com.xuecheng.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-04 14:27
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableFeignClients(basePackages = "com.xuecheng.content.feignclient")
@EnableHystrix
public class XuechengPlusContentApplication {
    public static void main(String[] args) {
        SpringApplication.run(XuechengPlusContentApplication.class);
    }
}
