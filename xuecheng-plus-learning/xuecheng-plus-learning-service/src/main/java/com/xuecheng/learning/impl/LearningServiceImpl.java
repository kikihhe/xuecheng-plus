package com.xuecheng.learning.impl;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.learning.service.LearningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-03-05 19:49
 */
@Service
@Slf4j
public class LearningServiceImpl implements LearningService {
    /**
     * @description 获取教学视频
     * @param courseId 课程id
     * @param teachplanId 课程计划id
     * @param mediaId 视频文件id
     * @return com.xuecheng.base.model.RestResponse<java.lang.String>
     * @author Mr.M
     * @date 2022/10/5 9:08
     */
    @Override
    public RestResponse<String> getVideo(String userId, Long courseId, Long teachplanId, String mediaId) {
        return null;
    }
}
