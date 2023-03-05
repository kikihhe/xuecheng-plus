package com.xuecheng.learning.feignclient;

import com.xuecheng.content.model.po.CoursePublish;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-03-05 20:05
 */
@FeignClient(value = "content-api", fallbackFactory = CoursePublishFallbackClient.class)
public interface CoursePublishClient {
    // 此方法只提供给learning微服务调用
    @ResponseBody
    @GetMapping("/content/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId);

}
