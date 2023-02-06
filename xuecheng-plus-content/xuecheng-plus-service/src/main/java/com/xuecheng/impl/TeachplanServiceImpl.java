package com.xuecheng.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.mapper.TeachplanMapper;
import com.xuecheng.servicce.TeachplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 19:45
 */
@Service
public class TeachplanServiceImpl extends ServiceImpl<TeachplanMapper, Teachplan> implements TeachplanService {
    @Autowired
    private TeachplanMapper teachplanMapper;
    @Override
    public List<TeachplanDto> getTeachplanTree(Long courseId) {
        return teachplanMapper.selectTreeNodes(courseId);
    }
}
