package com.xuecheng.content.model.po;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-05 15:26
 */
@Data
public class CourseMarket implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 收费方式
     */
    private String charge;


    private Float price;

    private Float originalPrice;

    private String qq;

    private String wechat;

    private String phone;

    /**
     * 有效天数
     */
    private Integer validDays;

}
