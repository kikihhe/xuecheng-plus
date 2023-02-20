package com.xuecheng.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.servicce.CourseBaseService;
import com.xuecheng.servicce.CoursePublishService;
import com.xuecheng.servicce.TeachplanService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:35
 */
@Service
@Transactional
public class CoursePublishServiceImpl implements CoursePublishService {
    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Override
    public CoursePreviewDto getCoursePreviewInfo(Long courseId) {
        // 根据课程id获取基本信息,营销信息
        CourseBaseInfoDto courseBaseInfo = courseBaseService.getCourseBaseInfo(courseId);

        // 根据客课程id获取该课程的教学计划
        List<TeachplanDto> teachplanTree = teachplanService.getTeachplanTree(courseId);

        // 返回结果
        return new CoursePreviewDto(courseBaseInfo, teachplanTree);
    }

    /**
     * 提交审核
     * @param companyId
     * @param courseId
     */
    @Override
    public void commitAudit(Long companyId, Long courseId) throws JsonProcessingException {
        // 查询课程是否已经提交审核过
        CourseBase courseBase = courseBaseMapper.selectById(courseId);
        String auditStatus = courseBase.getAuditStatus();
        if ("202003".equals(auditStatus)) {
            throw new RuntimeException("课程已提交审核，待审核后可以再次提交");
        }
        // 只能提交本机构的课程审核
        if (!companyId.equals(courseBase.getCompanyId())) {
            throw new RuntimeException("只能提交本机构的课程审核");
        }
        // 课程图片未填写
        if (StringUtils.isEmpty(courseBase.getPic())) {
            throw new RuntimeException("课程图片未填写!无法提交审核");
        }
        // 课程教学计划填写
        List<TeachplanDto> teachplanTree = teachplanService.getTeachplanTree(courseId);
        if (Objects.isNull(teachplanTree) || teachplanTree.size() <= 0) {
            throw new RuntimeException("请先将课程教学计划填写完毕");
        }

        // 封装信息
        CoursePublishPre publishPre = new CoursePublishPre();
        //  1.基本信息
        CourseBaseInfoDto info = courseBaseService.getCourseBaseInfo(courseId);
        //  2.课程计划信息转为json
        List<TeachplanDto> tree = teachplanService.getTeachplanTree(courseId);
        String treeJson = new ObjectMapper().writeValueAsString(tree);
        //  3.课程营销信息转为json
        CourseMarket courseMarket = courseMarketMapper.selectById(courseId);
        String marketJson = new ObjectMapper().writeValueAsString(courseMarket);

        // 开始封装
        BeanUtils.copyProperties(info, publishPre);
        publishPre.setMarket(marketJson);
        publishPre.setTeachplan(treeJson);

    }
}
