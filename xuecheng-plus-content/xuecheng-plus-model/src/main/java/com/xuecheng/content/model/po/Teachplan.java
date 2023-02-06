package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 15:20
 */
@Data
@TableName("teachplan")
public class Teachplan implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 课程计划id 主键
     */
    private Long id;

    /**
     * 章节名称
     */
    private String pname;

    /**
     * 课程计划父级id
     */
    private Long parentid;

    /**
     * 层级，分为1 2 3级
     */
    private Integer grade;

    /**
     * 课程类型:
     * 1 视频
     * 2 文档
     */
    private String mediaType;

    /**
     * 开始直播时间
     */
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime endTime;

    /**
     * 章节及课程介绍
     */
    private String description;

    /**
     * 章节时长
     */
    private String timelength;

    /**
     * 排序字段
     */
    private Integer orderby;

    /**
     * 章节所属课程id
     */
    private Long courseId;

    /**
     * 课程发布标识
     */
    private Long coursePubId;

    /**
     * 章节的状态
     * 1 正常
     * 0 删除
     */
    private Integer status;

    /**
     * 是否支持试学或预览
     */
    private String isPreview;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createDate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime changeDate;

}
