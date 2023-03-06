//package com.xuecheng.content.feignclient;
//
//import com.xuecheng.search.po.CourseIndex;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * @author : 小何
// * @Description :
// * @date : 2023-02-23 15:01
// */
//@FeignClient(value = "search", fallbackFactory = SearchServiceClientFallbackFactory.class)
//@RequestMapping("/search")
//public interface SearchServiceClient {
//
//    @PostMapping("/index/course")
//    public Boolean add(@RequestBody CourseIndex courseIndex);
//
//}
