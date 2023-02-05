package com.xuecheng.content.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 新增课程的接收参数
 */
@Data
@ApiModel(value = "AddCourseDto", description = "新增课程基本信息的传参")
public class AddCourseDto {
    /**
     * 课程名称
     */
    @NotEmpty(message = "课程名称不能为空")
    @ApiModelProperty(required = true, value = "课程名称")
    private String name;

    /**
     * 适用人群
     */
    @NotEmpty(message = "适用人群不能为空")
    @Size(message = "适用人群内容过少", min = 10)
    @ApiModelProperty(value = "课程适用人群", required = true)
    private String users;

    /**
     * 课程标签
     */
    private String tags;


    /**
     * 大分类
     */
    @NotEmpty(message = "课程分类不能为空")
    @ApiModelProperty(value = "课程大分类", required = true)
    private String mt;

    /**
     * 小分类
     */
    @NotEmpty(message = "课程分类不能为空")
    @ApiModelProperty(value = "课程小分类", required = true)
    private String st;

    /**
     * 课程等级
     */
    @NotEmpty(message = "课程等级不能为空")
    @ApiModelProperty(value = "课程等级", required = true)
    private String grade;

    /**
     * 教学方式
     */
    @ApiModelProperty(value = "教学方式(普通、录播、直播)", required = true)
    private String teachmode;

    /**
     * 课程介绍
     */
    @ApiModelProperty(value = "课程介绍")
    private String description;

    /**
     * 课程图片
     */
    @ApiModelProperty(value = "课程图片", required = true)
    private String pic;

    /**
     * 收费规则
     */
    @NotEmpty(message = "收费规则不能为空")
    @ApiModelProperty(value = "收费规则, 对应数据字典", required = true)
    private String charge;

    /**
     * 价格，现价
     */
    @ApiModelProperty(value = "价格")
    @NotNull(message = "现价不能为空")
    private Float price;

    /**
     * 原价
     */
    @ApiModelProperty(value = "原价")
    @NotNull(message = "原价不能为空")
    private Float originalPrice;

    /**
     * qq号, 将会使用自定义注解完成校验
     */
    @ApiModelProperty(value = "qq")
    private String qq;

    /**
     * 微信号, 将会使用自定义注解完成校验
     */
    @ApiModelProperty(value = "微信")
    private String wechat;

    /**
     * 电话号码, , 将会使用自定义注解完成校验
     */
    @ApiModelProperty(value = "电话号码")
    private String phone;

    /**
     * 课程有效期
     */
    @ApiModelProperty(value = "课程有效期")
    private Integer validDays;



}
