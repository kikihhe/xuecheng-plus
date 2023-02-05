package com.xuecheng.content.model.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-05 10:50
 */
@Data
@TableName("course_category")
public class CourseCategory implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 分类名称
     */
    private String name;

    /**
     * 分类标签，分类标签默认和名称一样
     */
    private String label;

    /**
     * 父节点id
     */
    private  String parentid;

    /**
     * 是否显示
     */
    private Integer isShow;

    /**
     * 排序字段
     */
    private Integer orderBy;

    /**
     * 是否为叶子节点
     */
    private Integer isLeaf;
}
