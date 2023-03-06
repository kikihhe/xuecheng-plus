package com.xuecheng.content.feignclient;

//import com.xuecheng.search.po.CourseIndex;
//import feign.hystrix.FallbackFactory;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.RequestMapping;
//
///**
// * @author : 小何
// * @Description :
// * @date : 2023-02-23 15:02
// */
//@Component
//@Slf4j
//public class SearchServiceClientFallbackFactory implements FallbackFactory<SearchServiceClient> {
//    @Override
//    public SearchServiceClient create(Throwable throwable) {
//        log.error("调用搜索远程服务出现熔断，实行降级处理, 任何业务返回null, 异常信息: {}", throwable.getCause());
//        return new SearchServiceClient() {
//            @Override
//            public Boolean add(CourseIndex courseIndex) {
//                return null;
//            }
//        };
//    }
//}
