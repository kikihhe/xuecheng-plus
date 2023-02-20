package com.xuecheng.content.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoursePreviewDto {
    /**
     * 课程的基本信息
     */
    private CourseBaseInfoDto courseBase;

    /**
     * 课程的教学计划
     */
    private List<TeachplanDto> teachplans;
}
