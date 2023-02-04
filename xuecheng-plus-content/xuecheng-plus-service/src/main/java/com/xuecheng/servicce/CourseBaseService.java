package com.xuecheng.servicce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-04 14:17
 */
public interface CourseBaseService extends IService<CourseBase> {


    /**
     * 查询所有课程
     * @param params 分页信息
     * @param dto 查询课程信息
     * @return 分页结果类
     */
    public PageResult<CourseBase> queryCourseBaseList(PageParams params, QueryCourseParamsDto dto);
}
