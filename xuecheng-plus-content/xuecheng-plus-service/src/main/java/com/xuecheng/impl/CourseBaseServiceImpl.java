package com.xuecheng.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.servicce.CourseBaseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-04 14:18
 */
@Service
public class CourseBaseServiceImpl extends ServiceImpl<CourseBaseMapper, CourseBase> implements CourseBaseService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;


    /**
     * 查询所有课程
     * @param params 分页信息
     * @param dto 查询课程信息
     * @return 分页结果类
     */
    @Override
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto dto) {
        // 1. 根据条件查询课程
        LambdaQueryWrapper<CourseBase> lambda = new LambdaQueryWrapper<>();
        // 1.1 根据名称模糊查询
        lambda.like(StringUtils.isNotEmpty(dto.getCourseName()), CourseBase::getName, dto.getCourseName());
        // 1.2 根据审核状态查询
        lambda.eq(StringUtils.isNotEmpty(dto.getAuditStatus()), CourseBase::getAuditStatus, dto.getAuditStatus());
        // 1.3 根据课程发布状态
        lambda.eq(StringUtils.isNotEmpty(dto.getPublishStatus()), CourseBase::getStatus, dto.getPublishStatus());

        // 2. 分页条件
        Page<CourseBase> page = new Page<>(params.getPageNo(), params.getPageSize());


        // 查询
        Page<CourseBase> pageResult = courseBaseMapper.selectPage(page, lambda);

        // List<T> items, long counts, long page, long pageSize
        PageResult<CourseBase> result = new PageResult<CourseBase>(
                pageResult.getRecords(),
                pageResult.getTotal(),
                params.getPageNo(),
                params.getPageSize()
        );
        return result;
    }
}
