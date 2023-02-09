package com.xuecheng.media.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:20
 */
@Data
public class QueryMediaParamsDto {
    /**
     * 文件名称
     */
    @ApiModelProperty("媒资文件名称")
    private String filename;

    /**
     * 文件类型
     */
    @ApiModelProperty("媒资文件类型")
    private String fileType;

    /**
     * 审核状态
     */
    @ApiModelProperty("审核状态")
    private String auditStatus;
}
