package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 课程分类相关接口, 将课程所有分类构建为树结构返回给前端展示
 */
@RestController
@Slf4j
@Api("课程分类相关接口")
public class CourseCategoryController {
    public List<CourseCategoryTreeDto> queryTreeNodes() {
        return null;
    }
}
