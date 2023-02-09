package com.xuecheng.media.model.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.omg.CORBA.IDLType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 17:44
 */
@Data
@TableName("media_files")
public class MediaFiles implements Serializable {
    private static final long serialVersion = 1L;

    /**
     * 主键, 文件的md5值
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 机构id
     */
    private Long companyId;

    /**
     * 机构名称
     */
    private String companyName;

    /**
     * 文件名称
     */
    private String filename;

    /**
     * 文件类型(文档、音频、视频)
     */
    private String fileType;

    /**
     * 标签
     */
    private String tags;

    /**
     * 存储到minio的哪个桶中
     * mediafiles: 文件
     * vedio: 视频
     */
    private String bucket;

    /**
     * 存储路径
     */
    private String filePath;

    /**
     * 文件标识
     */
    private String fileId;

    /**
     * 文件访问地址
     */
    private String url;

    /**
     * 上传人的姓名
     */
    private String username;

    /**
     * 上传时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

    /**
     * 状态；1:未处理， 2:视频处理完成
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 审核状态
     */
    private String auditStatus;

    /**
     * 审核意见
     */
    private String auditMind;

    /**
     * 文件大小
     */
    private Long fileSize;

}
