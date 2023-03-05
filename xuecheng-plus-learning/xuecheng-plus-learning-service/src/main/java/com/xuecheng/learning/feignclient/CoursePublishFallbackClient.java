package com.xuecheng.learning.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-03-05 20:07
 */
@Slf4j
@Component
public class CoursePublishFallbackClient implements FallbackFactory<CoursePublishClient> {
    @Override
    public CoursePublishClient create(Throwable throwable) {
        log.error("learning服务调用content服务时出现熔断,信息为: {}", throwable);
        return null;
    }
}
