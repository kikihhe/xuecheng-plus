package com.xuecheng.base.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowCredentials(true)
                .allowedHeaders("*")
                .maxAge(1000000);
        // 允许所有请求跨域
//        registry.addMapping("/**")
//                // 设置允许所有跨域的域名
//                .allowedOriginPatterns("*")
//                // 允许cookie
//                .allowCredentials(true)
//                // 设置允许的请求方法
//                .allowedMethods("GET", "POST", "PUT", "DELETE")
//                // 设置允许的Header属性
//                .allowedHeaders("*")
//                // 跨域允许时间
//                .maxAge(3600);
    }


}
