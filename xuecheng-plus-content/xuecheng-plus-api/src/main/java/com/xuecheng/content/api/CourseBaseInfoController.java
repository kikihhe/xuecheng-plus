package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.servicce.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 课程管理相关接口
 */
@Api(value = "课程管理相关接口", tags = "课程管理相关的接口")
@RestController
public class CourseBaseInfoController {

    @Autowired
    private CourseBaseService courseBaseService;

    /**
     * 查询信息 通过json传入
     * 分页信息 通过queryString传入
     * @param queryCourseParamsDto 查询信息
     * @param params 分页信息
     * @return 返回统一的分页结果类 PageResult
     */
    @ApiOperation("课程查询接口")
    @PostMapping("/course/list")
    public PageResult<CourseBase> list(@RequestBody QueryCourseParamsDto queryCourseParamsDto,
                                       PageParams params) {

        return courseBaseService.queryCourseBaseList(params, queryCourseParamsDto);

    }


    /**
     * 新增课程
     * @param addCourseDto 新增的课程的信息
     * @return 返回该新增的课程的信息以
     */
    @ApiOperation("新增课程")
    @PostMapping("/course")
    public CourseBaseInfoDto addCourse(@RequestBody AddCourseDto addCourseDto) {
        // 获取当前登录用户所属的培训机构的id
        // TODO 当前是假数据
        Long companyId = 22L;

        //
        return courseBaseService.createCourse(companyId, addCourseDto);

    }

}
