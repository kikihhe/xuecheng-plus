package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.CourseCategory;
import lombok.Data;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-05 11:01
 */
@Data
public class CourseCategoryTreeDto extends CourseCategory {
    /**
     * 本节点的所有子节点
     */
    List  childrenTreeNodes;

}
