package com.xuecheng.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-26 17:11
 */
@Slf4j
@Component
public class CheckCodeClientFallbackFactory implements FallbackFactory<CheckCodeClient> {
    @Override
    public CheckCodeClient create(Throwable throwable) {
        return new CheckCodeClient() {
            @Override
            public Boolean verify(String key, String code) {
                log.error("调用验证码服务异常,执行熔断降级, 异常信息:{}", throwable);
                return null;
            }
        };
    }
}
