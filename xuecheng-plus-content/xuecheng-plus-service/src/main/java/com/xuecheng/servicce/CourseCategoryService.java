package com.xuecheng.servicce;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-05 11:46
 */
public interface CourseCategoryService extends IService<CourseCategory> {
    /**
     * 查询课程分类结果树
     * @param id
     * @return
     */
    public List<CourseCategoryTreeDto> queryTreeNode();

}
