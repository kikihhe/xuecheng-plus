package com.xuecheng.content.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.content.mapper.TeachplanMapper;
import com.xuecheng.content.mapper.TeachplanMediaMapper;
import com.xuecheng.content.servicce.TeachplanService;
import com.xuecheng.content.model.dto.BindTeachplanMediaDto;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    @Autowired
    private TeachplanMediaMapper teachplanMediaMapper;

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

    /**
     * 实现媒资与教学计划的绑定
     * @param dto
     */
    @Override
    public void associationMedia(BindTeachplanMediaDto dto) {
        // 根据教学计划id查询出该教学计划，如果没有，报错。
        // 如果教学计划不是二级菜单，不让绑定媒资
        Long id = dto.getTeachplanId();
        Teachplan teachplan = teachplanMapper.selectById(id);
        if (Objects.isNull(teachplan)) {
            throw new RuntimeException("该教学计划不存在");

        }
        if (teachplan.getGrade() != 2) {
            throw new RuntimeException("只有二级教学计划才能绑定媒资文件!");
        }


        // 先删除原先该教学计划绑定的媒资文件
        LambdaQueryWrapper<TeachplanMedia> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TeachplanMedia::getTeachplanId, id);
        teachplanMediaMapper.delete(queryWrapper);
        // 再将此媒资文件与该教学计划绑定
        TeachplanMedia teachplanMedia = new TeachplanMedia();
        teachplanMedia.setTeachplanId(id);
        teachplanMedia.setMediaId(dto.getMediaId());
        teachplanMedia.setMediaFilename(dto.getFileName());
        teachplanMedia.setCourseId(teachplan.getCourseId());
        teachplanMedia.setCreateDate(LocalDateTime.now());

        teachplanMediaMapper.insert(teachplanMedia);


    }
}
