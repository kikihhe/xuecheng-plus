package com.xuecheng.servicce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CoursePreviewDto;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:34
 */

public interface CoursePublishService {
    public CoursePreviewDto getCoursePreviewInfo(Long courseId);
}
