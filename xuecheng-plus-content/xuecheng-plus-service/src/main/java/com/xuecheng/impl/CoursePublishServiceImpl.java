package com.xuecheng.impl;

import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.servicce.CourseBaseService;
import com.xuecheng.servicce.CoursePublishService;
import com.xuecheng.servicce.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:35
 */
@Service
public class CoursePublishServiceImpl implements CoursePublishService {
    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanService teachplanService;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        // 根据课程id获取基本信息,营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);

        // 根据客课程id获取该课程的教学计划
        List<TeachplanDto> teachplanTree = teachplanService.getTeachplanTree(courseId);

        // 返回结果
        return new CoursePreviewDto(courseBaseInfo, teachplanTree);
    }
}
