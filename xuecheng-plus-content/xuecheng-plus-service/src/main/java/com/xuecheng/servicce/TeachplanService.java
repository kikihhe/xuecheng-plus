package com.xuecheng.servicce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 19:44
 */
public interface TeachplanService extends IService<Teachplan> {
    public List<TeachplanDto> getTeachplanTree(Long courseId);
}
