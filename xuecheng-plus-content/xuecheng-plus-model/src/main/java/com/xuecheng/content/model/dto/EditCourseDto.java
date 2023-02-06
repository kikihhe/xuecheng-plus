package com.xuecheng.content.model.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 00:55
 */
@Data
public class EditCourseDto extends AddCourseDto {
    @NotNull(message = "课程id不能为空!")
    private Long id;
}
