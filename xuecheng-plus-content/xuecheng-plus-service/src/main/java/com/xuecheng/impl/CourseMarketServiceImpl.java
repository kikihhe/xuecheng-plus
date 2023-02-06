package com.xuecheng.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.servicce.CourseMarketService;
import org.springframework.stereotype.Service;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 11:37
 */
@Service
public class CourseMarketServiceImpl extends ServiceImpl<CourseMarketMapper, CourseMarket> implements CourseMarketService {
}
