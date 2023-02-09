package com.xuecheng.media.model.dto;

import lombok.Data;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 19:03
 */
@Data
public class UploadFileParamsDto {
    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件的content-type
     */
    private String contentType;

    /**
     * 文件类型(文档、音频、视频)
     */
    private String fileType;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 标签
     */
    private String tags;

    /**
     * 上传人姓名
     */
    private String username;

    /**
     * 备注
     */
    private String remark;


}
