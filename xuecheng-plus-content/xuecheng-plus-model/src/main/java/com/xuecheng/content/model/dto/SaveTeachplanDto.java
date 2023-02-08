package com.xuecheng.content.model.dto;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class SaveTeachplanDto {

    /**
     * 课程计划id（章/小结的id）
     */
    private Long id;

    /**
     * 课程计划名称
     */
    private String pname;


    /**
     * 父目录的id
     */
    private Long parentid;

    /**
     * 层级
     */
    private Integer grade;

    /**
     * 直播开始时间
     */
    private LocalDateTime startTime;

    /**
     * 直播结束时间
     */
    private LocalDateTime endTime;

    /**
     * 排序字段
     */
    private Integer orderby;

    /**
     * 该课程计划所属的课程id
     */
    private Long courseId;


    /**
     * 课程发布标识
     */
    private Long coursePubId;

    /**
     * 是否支持试学或预览(试看)
     */
    private String isPreview;

}
