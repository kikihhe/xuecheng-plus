package com.xuecheng.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xuecheng.content.model.dto.CourseBaseInfoDto;
import com.xuecheng.content.model.dto.CoursePreviewDto;
import com.xuecheng.content.model.dto.TeachplanDto;
import com.xuecheng.content.model.po.CourseBase;
import com.xuecheng.content.model.po.CourseMarket;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.content.model.po.CoursePublishPre;
import com.xuecheng.mapper.CourseBaseMapper;
import com.xuecheng.mapper.CourseMarketMapper;
import com.xuecheng.mapper.CoursePublishMapper;
import com.xuecheng.mapper.CoursePublishPreMapper;
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
public class CoursePublishServiceImpl extends ServiceImpl<CoursePublishMapper, CoursePublish> implements CoursePublishService {
    @Autowired
    private CourseBaseService courseBaseService;

    @Autowired
    private TeachplanService teachplanService;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseMarketMapper courseMarketMapper;

    @Autowired
    private CoursePublishPreMapper coursePublishPreMapper;
    @Autowired
    private CoursePublishMapper coursePublishMapper;

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
        publishPre.setStatus("202003"); // 状态为未审核

        // 如果该数据正在预发布表中，说明之前已经提交审核,说明这次该修改了
        CoursePublishPre publishPre1 = coursePublishPreMapper.selectById(courseId);
        if (Objects.isNull(publishPre1)) {
            coursePublishPreMapper.insert(publishPre);
        } else {
            coursePublishPreMapper.updateById(publishPre);
        }

    }
    public void publish(Long companyId, Long courseId) {
        CoursePublishPre course = coursePublishPreMapper.selectById(courseId);
        // 1. 将课程信息从预发布表插入到发布表
        saveCoursePublish(course);



        // 2. 将课程信息插入到mq_message表


        // 3. 将课程信息从预发布表中删除

    }

    private void saveCoursePublish(CoursePublishPre course) {
        CoursePublish cp = new CoursePublish();
        BeanUtils.copyProperties(course, cp);
        cp.setStatus("203002");
        CoursePublish coursePublish = coursePublishMapper.selectById(course.getId());
        // 如果已经发布，则更新; 如果未发布，就插入
        if (Objects.isNull(coursePublish)) {
            coursePublishMapper.insert(cp);
        } else {
            coursePublishMapper.updateById(cp);
        }
        // 更改课程基本信息中的发布状态
        CourseBase courseBase = courseBaseMapper.selectById(cp.getId());
        courseBase.setStatus("203002");// 已发布
        courseBaseMapper.updateById(courseBase);
    }
    private void saveCoursePublishMessage(Long courseId) {

    }
    private void deleteCoursePublishPre(Long courseId) {
        coursePublishPreMapper.deleteById(courseId);
    }
}
