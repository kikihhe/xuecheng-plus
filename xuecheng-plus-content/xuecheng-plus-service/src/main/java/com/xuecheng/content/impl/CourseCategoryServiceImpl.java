package com.xuecheng.content.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.CourseCategoryMapper;
import com.xuecheng.content.model.dto.CourseCategoryTreeDto;
import com.xuecheng.content.model.po.CourseCategory;
import com.xuecheng.content.servicce.CourseCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-05 11:46
 */
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategoryMapper, CourseCategory> implements CourseCategoryService {
    @Autowired
    private CourseCategoryMapper courseCategoryMapper;

    /**
     * 查询课程分类结果树
     * @param id 需要查询的根节点，一般为"1"
     * @return 返回的列表
     */
    @Override
    public List<CourseCategoryTreeDto> queryTreeNode() {
        return courseCategoryMapper.selectTreeNodes();
    }
}
