package com.xuecheng.content.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.content.model.dto.AddCourseDto;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.EditCourseDto;
import com.xuecheng.content.model.dto.QueryCourseParamsDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.servicce.CourseBaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public CourseBaseInfoDto addCourse(@RequestBody @Validated AddCourseDto addCourseDto) throws RuntimeException {
        // 获取当前登录用户所属的培训机构的id
        // TODO 当前是假数据, 公司id未完成,待登录接口完成过后补充
        Long companyId = 22L;

        // 执行插入操作
        return courseBaseService.createCourse(companyId, addCourseDto);

    }


    /**
     * 修改之前查询课程信息
     * @param courseId 课程id
     * @return 课程信息
     */
    @GetMapping("/course/{courseId}")
    public CourseBaseInfoDto getCourseInfoById(@PathVariable Long courseId) {
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);

        return courseBaseInfo;

    }

    /**
     * 修改课程
     * @param companyId 用于校验，本机构只能修改本机构的课程
     * @param dto 修改后的信息
     * @return 返回修改后的信息
     */
    @PutMapping("/course")
    public CourseBaseInfoDto modifyCourseBase(Long companyId, @RequestBody @Validated EditCourseDto dto) {
        return courseBaseService.updateCourseBaseInfo(companyId, dto);
    }

    public static void main(String[] args) {
        System.out.println(new BCryptPasswordEncoder().encode("11111"));
    }

}
