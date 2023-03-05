package com.xuecheng.learning.service;

import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-03-05 21:19
 */

public interface MyCourseTablesService {
    /**
     * 添加选课
     * @param userId 学生id
     * @param courseId 课程id
     * @return
     */
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId);

    /**
     * 登录用户是否有课程的学习资格
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId);
}
