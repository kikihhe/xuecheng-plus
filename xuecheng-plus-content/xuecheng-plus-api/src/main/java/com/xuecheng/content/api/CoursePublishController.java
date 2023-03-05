package com.xuecheng.content.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.content.servicce.CoursePublishPreService;
import com.xuecheng.content.servicce.CoursePublishService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:09
 */
@Controller
public class CoursePublishController {
    @Autowired
    private CoursePublishService coursePublishService;
    @Autowired
    private CoursePublishPreService coursePublishPreService;

    /**
     * 预览课程
     * @param courseId 需要预览的课程的id
     * @return
     */
    @GetMapping("/coursepreview/{courseId}")
    public ModelAndView preview(@PathVariable("courseId") Long courseId) {
        ModelAndView modelAndView = new ModelAndView();
        // 获取课程的信息
        CoursePreviewDto coursePreviewInfo = coursePublishService.getCoursePreviewInfo(courseId);
        modelAndView.addObject("model", coursePreviewInfo);
        modelAndView.setViewName("course_template");
        return modelAndView;
    }

    /**
     * 提交审核
     * @param courseId 提交审核的课程的id
     */
    @ResponseBody
    @PostMapping("/courseaudit/commit/{courseId}")
    public void commitAudit(@PathVariable("courseId") Long courseId) throws JsonProcessingException {
        Long companyId = 1232141425L;
        coursePublishService.commitAudit(companyId, courseId);

    }


    /**
     * 课程发布
     * 使用本地事务将需要发布的课程写入 course_base 与 mq_message中
     * course_base保证别人能在数据库中查到课程信息
     * mq_message会被xxl-job扫描，执行 redis es minio 的缓存
     * @param courseId
     */
    @ApiOperation("课程发布")
    @ResponseBody
    @PostMapping("/coursepublish/{courseId}")
    public void coursePublish(@PathVariable("courseId") Long courseId) {

        Long companyId = 1232141425L;
        // 提交课程审核并且通过才能发布
        CoursePublishPre coursePublishPre = coursePublishPreService.getById(courseId);
        if (Objects.isNull(coursePublishPre)) {
            throw new RuntimeException("请先提交课程审核，审核通过才能发布");
        }
        if (!"202004".equals(coursePublishPre.getStatus())) {
            throw new RuntimeException("操作失败，课程正在审核");
        }
        // 本机构只能发布本机构的课程
        if (!companyId.equals(coursePublishPre.getCompanyId())) {
            throw new RuntimeException("本机构只能发布本机构的课程");
        }
        //
        coursePublishService.publish(companyId, courseId);

    }

    // 此方法只提供给learning微服务调用
    @ResponseBody
    @GetMapping("/r/coursepublish/{courseId}")
    public CoursePublish getCoursePublish(@PathVariable("courseId") Long courseId) {
        // 查询相应课程的发布信息
        return coursePublishService.getCoursePublish(courseId);
    }


}
