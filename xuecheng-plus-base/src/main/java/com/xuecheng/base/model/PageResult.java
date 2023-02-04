package com.xuecheng.base.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-04 11:41
 */
@Data
public class PageResult<T> implements Serializable {

    /**
     * 分页查询的数据列表
     */
    private List<T> items;

    /**
     * 总记录数
     */
    private long counts;

    /**
     * 当前页码
     */
    private long page;

    /**
     * 每页记录数
     */
    private long pageSize;

    public PageResult(List<T> items, long counts, long page, long pageSize) {
        this.items = items;
        this.counts = counts;
        this.page = page;
        this.pageSize = pageSize;
    }
}
