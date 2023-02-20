package com.xuecheng.servicce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xuecheng.content.model.dto.CoursePreviewDto;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:34
 */

public interface CoursePublishService {
    /**
     * 获取课程预览的基本信息
     * @param courseId 课程id
     * @return 课程信息: 课程基本信息、课程的教学计划
     */
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);

    /**
     * 提交审核
     * @param companyId
     * @param courseId
     */
    public void commitAudit(Long companyId, Long courseId) throws JsonProcessingException;


}
