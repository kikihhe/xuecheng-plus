package com.xuecheng.learning.api;//package com.xuecheng.learning.api;
//
//
import com.xuecheng.base.model.PageResult;
import com.xuecheng.learning.model.dto.MyCourseTableItemDto;
import com.xuecheng.learning.model.dto.MyCourseTableParams;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.service.MyCourseTablesService;
import com.xuecheng.learning.util.SecurityUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


/**
 * @author Mr.M
 * @version 1.0
 * @description 我的课程表接口
 * @date 2022/10/2 14:52
 */
@Api(value = "我的课程表接口", tags = "我的课程表接口")
@Slf4j
@RestController
public class MyCourseTablesController {

    private MyCourseTablesService myCourseTablesService;

    @Autowired
    public MyCourseTablesController(MyCourseTablesService myCourseTablesService) {
        this.myCourseTablesService = myCourseTablesService;
    }

    @ApiOperation("添加选课")
    @PostMapping("/choosecourse/{courseId}")
    public XcChooseCourseDto addChooseCourse(@PathVariable("courseId") Long courseId) {
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        String userId = user.getId();

        XcChooseCourseDto xcChooseCourseDto = myCourseTablesService.addChooseCourse(userId, courseId);
        return xcChooseCourseDto;

    }

    @PostMapping("/choosecourse/learnstatus/{courseId}")
    public XcCourseTablesDto getLearningStatus(@PathVariable("courseId") Long courseId) {
        // 查看当前登录状态。如果没有登陆，禁止学习
        SecurityUtil.XcUser user = SecurityUtil.getUser();
        if (Objects.isNull(user)) {
            throw new RuntimeException("请先登录");
        }
        XcCourseTablesDto learningStatus = myCourseTablesService.getLearningStatus(user.getId(), courseId);
        return learningStatus;
    }




}
