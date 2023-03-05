package com.xuecheng.orders.api;

import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.CoursePublishClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XuechengPlusOrdersApiApplicationTests {
    @Autowired
    private CoursePublishClient coursePublishClient;

    @Test
    void contextLoads() {
        CoursePublish coursePublish = coursePublishClient.getCoursePublish(18L);
        System.out.println(coursePublish);
    }

}
