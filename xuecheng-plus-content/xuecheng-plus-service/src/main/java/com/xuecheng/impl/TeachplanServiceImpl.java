package com.xuecheng.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.mapper.TeachplanMapper;
import com.xuecheng.servicce.TeachplanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    /** dto的id为null，则新增; 否则修改
     *      Null: 保存章节/小结 grade为1，章节; 否则为小结
     *      NotNull: 修改章节/小结
     * @param dto
     */
    @Override
    @Transactional
    public void saveTeachplan(SaveTeachplanDto dto) {
        Long id = dto.getId();
        Integer orderby = teachplanMapper.selectTeachplanOrderby(dto.getCourseId(), dto.getParentid());

        if (Objects.isNull(id)) {
            // 1. 如果id为空，插入
            // 1.1 设置章节的排序orderby字段
            Teachplan teachplan = new Teachplan();
            // 1.2 赋值属性，插入
            BeanUtils.copyProperties(dto, teachplan);
            teachplan.setOrderby(orderby);
            int insert = teachplanMapper.insert(teachplan);
            if (insert != 1) {
                throw new RuntimeException("新增失败!");
            }

        } else {
            Teachplan teachplan = new Teachplan();
            // 如果有id，修改
            // 赋值属性，插入
            BeanUtils.copyProperties(dto, teachplan);
            teachplan.setOrderby(orderby);
            int update = teachplanMapper.updateById(teachplan);
            if (update != 1) {
                throw new RuntimeException("修改失败,请重试!");
            }
        }

    }
}
