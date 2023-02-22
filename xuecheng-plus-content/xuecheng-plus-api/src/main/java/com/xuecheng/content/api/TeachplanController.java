package com.xuecheng.content.api;

import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.servicce.TeachplanService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 15:42
 */

@Slf4j
@RestController
@Api("课程计划，即课程的每一个章节")
public class TeachplanController {
    @Autowired
    private TeachplanService teachplanService;


    /**
     * 显示课程的章节列表
     * @param courseId 课程id
     * @return 返回树形章节列表
     */
    @GetMapping("/teachplan/{courseId}/tree-nodes")
    public List<TeachplanDto> getTreeNodes(@PathVariable Long courseId) {
        return teachplanService.getTeachplanTree(courseId);
    }

    /**
     * 添加课程计划
     * @param dto
     */
    @PostMapping("/teachplan")
    public void saveTeachplan(@RequestBody SaveTeachplanDto dto) {
        teachplanService.saveTeachplan(dto);
    }

    /**
     * 将媒资与教学计划绑定
     * @param dto
     */
    @PostMapping("/teachplan/association/media")
    public void associationMedia(@RequestBody @Validated BindTeachplanMediaDto dto) {
        teachplanService.associationMedia(dto);
    }



}
