package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-18 15:08
 */
@Data
@ApiModel(value = "BindTeachplanMediaDto", description = "教学计划-媒资绑定提交数据")
public class BindTeachplanMediaDto {

    /**
     * 媒资文件的id
     */
    @NotEmpty(message = "媒资文件id不能为空")
    private String mediaId;

    /**
     * 教学计划的id
     */
    @NotNull(message = "教学计划id不能为空")
    private Long teachplanId;

    /**
     * 文件名称
     */
    private String fileName;

}
