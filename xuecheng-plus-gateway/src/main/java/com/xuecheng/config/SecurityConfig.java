package com.xuecheng.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 配置过滤器链
 */

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    // 安全拦截
    @Bean
    public SecurityWebFilterChain webFluxSecurityFilterChain(ServerHttpSecurity http) {

        http.authorizeExchange()
                // 所有请求均可访问
                .pathMatchers("/**").permitAll()
                .anyExchange().authenticated();
        http.csrf().disable();
        return http.build();
    }
}
