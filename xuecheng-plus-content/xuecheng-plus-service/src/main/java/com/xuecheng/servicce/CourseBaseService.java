package com.xuecheng.servicce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
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

    /**
     * 新增课程
     * @param companyId 机构id, 当前登录用户的id
     * @param addCourseDto 新增的课程的信息
     * @return 新增的课程的信息
     */
    public CourseBaseInfoDto createCourse(Long companyId, AddCourseDto addCourseDto);


    /**
     * 根据id获取课程信息
     * @param id 课程id
     * @return 课程信息
     */
    public CourseBaseInfoDto getCourseBaseInfo(Long id);


    /**
     * 修改课程信息
     * @param companyId 课程所属机构名称
     * @param dto 修改后的信息
     * @return 返回修改后的信息
     */
    public CourseBaseInfoDto updateCourseBaseInfo(Long companyId, EditCourseDto dto);
}
