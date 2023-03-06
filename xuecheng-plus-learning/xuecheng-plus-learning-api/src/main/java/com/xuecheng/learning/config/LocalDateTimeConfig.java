package com.xuecheng.learning.config;

import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.time.format.DateTimeFormatter;
//
///**
// * @author : 小何
// * @Description :
// * @date : 2023-03-05 20:58
// */
//@Configuration
//public class LocalDateTimeConfig {
//
//    // LocalDateTime转String
//    @Bean
//    public LocalDateTimeSerializer localDateTimeSerializer() {
//        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    }
//
//    public LocalDateTimeDeserializer localDateTimeDeserializer() {
//        return new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//    }
//
//
//}
