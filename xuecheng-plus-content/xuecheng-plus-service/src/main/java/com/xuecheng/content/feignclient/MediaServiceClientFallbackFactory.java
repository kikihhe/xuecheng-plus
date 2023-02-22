package com.xuecheng.content.feignclient;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-22 19:55
 */
@Slf4j
@Component
public class MediaServiceClientFallbackFactory implements FallbackFactory<MediaServiceClient> {
    @Override
    public MediaServiceClient create(Throwable throwable) {
        log.error("MediaServiceFeign远程调用熔断, 异常信息: {}", throwable);
        return new MediaServiceClient() {
            @Override
            public String upload(MultipartFile filedata, String folder, String objectName) {
                return null;
            }
        };
    }
}
