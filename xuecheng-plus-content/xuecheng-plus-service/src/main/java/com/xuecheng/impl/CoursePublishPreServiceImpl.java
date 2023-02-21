package com.xuecheng.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.mapper.CoursePublishPreMapper;
import com.xuecheng.servicce.CoursePublishPreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-21 12:58
 */
@Service
public class CoursePublishPreServiceImpl extends ServiceImpl<CoursePublishPreMapper, CoursePublishPre> implements CoursePublishPreService {
    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;

//    private


}
