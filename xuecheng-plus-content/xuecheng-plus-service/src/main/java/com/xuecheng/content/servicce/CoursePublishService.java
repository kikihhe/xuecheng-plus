package com.xuecheng.content.servicce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.messagesdk.model.po.MqMessage;

import java.io.File;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:34
 */

public interface CoursePublishService extends IService<CoursePublish> {
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

    /**
     * 发布课程
     * @param companyId 机构id
     * @param courseId 课程id
     */
    public void publish(Long companyId, Long courseId);


    /**
     * @description 课程静态化
     * @param courseId  课程id
     * @return File 静态化文件
     */
    public File generateCourseHtml(Long courseId);

    /**
     * @description 上传课程静态化页面
     * @param file  静态化文件
     * @return void
     */
    public void  uploadCourseHtml(Long courseId,File file);

    public void saveCourseIndex(String courseId);


    public CoursePublish getCoursePublish(Long courseId);


}
