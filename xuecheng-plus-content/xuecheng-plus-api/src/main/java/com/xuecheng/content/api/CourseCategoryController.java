package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.servicce.CourseCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程分类相关接口, 将课程所有分类构建为树结构返回给前端展示
 */
@RestController
@Slf4j
@Api("课程分类相关接口")
public class CourseCategoryController {
    @Autowired
    private CourseCategoryService courseCategoryService;


    @ApiOperation("查询所有课程的分类,以树结构返回")
    @GetMapping("/course-category/tree-nodes")
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return courseCategoryService.queryTreeNode("1");
    }
}
