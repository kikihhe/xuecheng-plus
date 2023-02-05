package com.xuecheng.content.model.dto;

import lombok.Data;

/**
 * 新增课程api的返回值
 */
@Data
public class CourseBaseInfoDto {
    /**
     * 收费规则, 对应数据字典
     */
    private String charge;


    /**
     * 价格，现价
     */
    private Float price;

    /**
     * 价格，原价
     */
    private Float originalPrice;

    /**
     * 咨询qq
     */
    private String qq;

    /**
     * 咨询微信
     */
    private String wechat;

    /**
     * 有效期天数
     */
    private Integer validDays;

    /**
     * 大分类名称
     */
    private String mtName;

    /**
     * 小分类名称
     */
    private String stName;
}
