package com.xuecheng.servicce;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.content.model.dto.SaveTeachplanDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.Teachplan;

import java.util.List;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-06 19:44
 */
public interface TeachplanService extends IService<Teachplan> {
    /**
     * 获取课程的章节目录
     * @param courseId 课程id
     * @return 返回课程的章节目录
     */
    public List<TeachplanDto> getTeachplanTree(Long courseId);

    /** dto 的id为null，则新增; 否则修改
     *      Null: 保存章节/小结 grade为1，章节; 否则为小结
     *      NotNull: 修改章节/小结
     * @param dto
     */
    public void saveTeachplan(SaveTeachplanDto dto) ;
}
