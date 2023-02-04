package com.xuecheng.base.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * 分页参数模型类
 *
 */
@Data
@ToString
public class PageParams {

    /**
     * 当前页码默认值
     */
    public static final long DEFAULT_PAGE_CURRENT = 1l;

    /**
     * 每页记录数默认值
     */
    public static final long DEFAULT_PAGE_SIZE = 10l;

    /**
     * 当前页码
     */
    @ApiModelProperty("当前页码")
    private Long pageNo = DEFAULT_PAGE_CURRENT;

    /**
     * 每页记录数
     */
    @ApiModelProperty("每页记录数")
    private Long pageSize = DEFAULT_PAGE_SIZE;

}
