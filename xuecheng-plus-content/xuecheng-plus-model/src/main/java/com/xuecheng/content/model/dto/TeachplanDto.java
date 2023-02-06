package com.xuecheng.content.model.dto;

import com.xuecheng.content.model.po.Teachplan;
import com.xuecheng.content.model.po.TeachplanMedia;
import lombok.Data;

import java.util.List;

/**
 * 作为树形结构返回
 */
@Data
public class TeachplanDto extends Teachplan {
    private TeachplanMedia teachplanMedia;

    private List<TeachplanDto> teachPlanTreeNodes;


}
