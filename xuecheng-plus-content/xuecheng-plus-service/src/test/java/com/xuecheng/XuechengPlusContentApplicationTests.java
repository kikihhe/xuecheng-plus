//package com.xuecheng;
//
//import com.xuecheng.base.model.PageParams;
//import com.xuecheng.base.model.PageResult;
//import com.xuecheng.content.config.MultipartSupportConfig;
//import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
//import com.xuecheng.content.model.dto.QueryCourseParamsDto;
//import com.xuecheng.content.model.dto.TeachplanDto;
//import com.xuecheng.content.model.po.CourseBase;
//import com.xuecheng.content.feignclient.MediaServiceClient;
//import com.xuecheng.content.mapper.CourseBaseMapper;
//import com.xuecheng.content.mapper.CourseCategoryMapper;
//import com.xuecheng.content.mapper.TeachplanMapper;
//import com.xuecheng.content.servicce.CourseBaseService;
//import org.apache.commons.lang.StringUtils;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.util.List;
//
///**
// * @author : 小何
// * @Description :
// * @date : 2023-02-04 14:29
// */
//@SpringBootTest
//public class XuechengPlusContentApplicationTests {
//
//    @Autowired
//    private CourseBaseMapper courseBaseMapper;
//    @Autowired
//    private CourseBaseService courseBaseService;
//    @Autowired
//    private CourseCategoryMapper courseCategoryMapper;
//
//    @Autowired
//    private TeachplanMapper teachplanMapper;
//
//    @Autowired
////    private MediaServiceClient mediaServiceClient;
//
//
////    @Test
////    public void testMediaFeignClient() throws Exception {
////        File file = new File("D:\\JAVA-projects\\xuecheng-plus-project\\xuecheng-plus-content\\xuecheng-plus-api\\src\\main\\resources\\templates\\course_template.ftl");
////        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
////        String course = mediaServiceClient.upload(multipartFile, "course", "test.html");
////        System.out.println(course);
////
////    }
//
//    @Test
//    public void testCourseBaseMapper() {
//        CourseBase courseBase = courseBaseMapper.selectById(1);
//        System.out.println(courseBase);
//    }
//
//    @Test
//    public void testStringUtils() {
//        System.out.println(StringUtils.isBlank("牛"));
//    }
//    @Test
//    public void testCourseBaseServiceImpl() {
//        PageParams pageParams = new PageParams();
//        PageResult<CourseBase> courseBasePageResult = courseBaseService.queryCourseBaseList(pageParams, new QueryCourseParamsDto());
//        System.out.println(courseBasePageResult);
//
//    }
//    @Test
//    public void testCourseCategoryMapper() {
//        List<CourseCategoryTreeDto> courseCategoryTreeDtos = courseCategoryMapper.selectTreeNodes();
//        System.out.println(courseCategoryTreeDtos);
//    }
//
//    @Test
//    public void testTeachplanMapper() {
//        List<TeachplanDto> teachplanDtos = teachplanMapper.selectTreeNodes(1L);
//        System.out.println(teachplanDtos);
//    }
//
//
//
//}
