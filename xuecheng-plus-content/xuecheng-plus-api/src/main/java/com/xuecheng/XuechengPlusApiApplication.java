package com.xuecheng;

import com.google.common.collect.Maps;
import com.spring4all.swagger.EnableSwagger2Doc;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableSwagger2Doc
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.xuecheng.content.feignclient"})
public class XuechengPlusApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(XuechengPlusApiApplication.class, args);
    }

}
