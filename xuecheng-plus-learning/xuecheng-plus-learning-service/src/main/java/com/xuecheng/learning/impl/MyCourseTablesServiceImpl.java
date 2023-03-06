package com.xuecheng.learning.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xuecheng.content.model.po.CoursePublish;
import com.xuecheng.learning.feignclient.CoursePublishClient;
import com.xuecheng.learning.mapper.XcChooseCourseMapper;
import com.xuecheng.learning.mapper.XcCourseTablesMapper;
import com.xuecheng.learning.model.dto.XcChooseCourseDto;
import com.xuecheng.learning.model.dto.XcCourseTablesDto;
import com.xuecheng.learning.model.po.XcChooseCourse;
import com.xuecheng.learning.model.po.XcCourseTables;
import com.xuecheng.learning.service.MyCourseTablesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-03-05 21:21
 */
@Service
@Slf4j
public class MyCourseTablesServiceImpl implements MyCourseTablesService {
    private XcChooseCourseMapper xcChooseCourseMapper;
    private CoursePublishClient coursePublishClient;
    private XcCourseTablesMapper xcCourseTablesMapper;

    @Autowired
    public MyCourseTablesServiceImpl(XcChooseCourseMapper xcChooseCourseMapper, CoursePublishClient publishClient, XcCourseTablesMapper xcCourseTablesMapper) {
        this.coursePublishClient = publishClient;
        this.xcChooseCourseMapper = xcChooseCourseMapper;
        this.xcCourseTablesMapper = xcCourseTablesMapper;
    }
    @Transactional
    @Override
    public XcChooseCourseDto addChooseCourse(String userId, Long courseId) {
        // 查询课程信息
        CoursePublish coursePublish = coursePublishClient.getCoursePublish(courseId);
        // 获取课程的收费规则
        String charge = coursePublish.getCharge();

        XcChooseCourse xcChooseCourse = new XcChooseCourse();
        if ("201000".equals(charge)) {
            // 免费课程直接添加到选课记录表和我的课程表中
            // 1. 向选课记录表中插入
            xcChooseCourse.setUserId(userId);
            xcChooseCourse.setCourseId(courseId);
            xcChooseCourse.setCourseName(coursePublish.getName());
            xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
            xcChooseCourse.setCoursePrice(0F);
            xcChooseCourse.setRemarks(coursePublish.getRemark());
            xcChooseCourse.setCoursePrice(coursePublish.getPrice());
            xcChooseCourse.setValidDays(coursePublish.getValidDays());
            xcChooseCourse.setValidtimeStart(LocalDateTime.now());
            // 设置选课状态为未支付
            xcChooseCourse.setOrderType("700001");
            int insert = xcChooseCourseMapper.insert(xcChooseCourse);
            if(insert <= 0) {
                log.error("课程向选课记录表中插入时出错，课程信息为: {}, 选客人: {}", xcChooseCourse, userId);
                throw new RuntimeException("选课失败，请重试");
            }
            // 2. 向我的课程表中插入
            XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
            if (!Objects.isNull(xcCourseTables)) {
                // 如果已经存在, 不插入
            } else {
                // 如果不存在，插入
                xcCourseTables.setChooseCourseId(xcChooseCourse.getId());
                xcCourseTables.setUserId(userId);
                xcCourseTables.setCourseId(courseId);
                xcCourseTables.setCompanyId(xcChooseCourse.getCompanyId());
                xcCourseTables.setCourseName(xcChooseCourse.getCourseName());
                xcCourseTables.setCreateDate(LocalDateTime.now());
                xcCourseTables.setValidtimeStart(xcChooseCourse.getValidtimeStart());
                xcCourseTables.setValidtimeEnd(xcChooseCourse.getValidtimeEnd());
                xcCourseTables.setCourseType(xcChooseCourse.getOrderType());
                int insert1 = xcCourseTablesMapper.insert(xcCourseTables);
                if(insert1 <= 0) {
                    log.error("免费选课插入我的课程表时出现错误,未插入成功,courseId:{}, userId:{} ", courseId, userId);
                    throw new RuntimeException("选课失败，请重试!");
                }
            }

        } else {
            // 收费课程只向 选课记录表 中添加记录
            xcChooseCourse.setUserId(userId);
            xcChooseCourse.setCourseId(courseId);
            xcChooseCourse.setCourseName(coursePublish.getName());
            xcChooseCourse.setCompanyId(coursePublish.getCompanyId());
            xcChooseCourse.setCoursePrice(coursePublish.getPrice());
            xcChooseCourse.setRemarks(coursePublish.getRemark());
            xcChooseCourse.setCoursePrice(coursePublish.getPrice());
            xcChooseCourse.setValidDays(coursePublish.getValidDays());
            xcChooseCourse.setValidtimeStart(LocalDateTime.now());
            // 设置选课类型为收费类型
            xcChooseCourse.setOrderType("700002");
            // 设置支付状态为待支付
            xcChooseCourse.setStatus("701002");

            int insert = xcChooseCourseMapper.insert(xcChooseCourse);
            if (insert <= 0) {
                log.error("选择收费课程时插入到选课记录表时出现错误，userId: {}, courseId: {}", userId, courseId);
                throw new RuntimeException("选课失败，请重新选择");

            }
        }

        XcChooseCourseDto xcChooseCourseDto = new XcChooseCourseDto();

        BeanUtils.copyProperties(xcChooseCourse, xcChooseCourseDto);
        // 获取用户对此课程的学习资格
        xcChooseCourseDto.setLearnStatus(getLearningStatus(userId, courseId).getLearnStatus());
        return xcChooseCourseDto;
    }


    /**
     * 查询这个课表我是否已经选择过了
     * @param userId
     * @param courseId
     * @return
     */
    public XcCourseTables getXcCourseTables(String userId, Long courseId) {
        LambdaQueryWrapper<XcCourseTables> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(XcCourseTables::getUserId, userId)
                .eq(XcCourseTables::getCourseId, courseId);
        return xcCourseTablesMapper.selectOne(lambdaQueryWrapper);
    }

    /**
     * 查询用户是否有资格学习该课程
     * @param userId 用户id
     * @param courseId 课程id
     * @return
     */
    @Override
    public XcCourseTablesDto getLearningStatus(String userId, Long courseId) {
        // 从我的课程表中查看课程
        XcCourseTables xcCourseTables = getXcCourseTables(userId, courseId);
        XcCourseTablesDto xcCourseTablesDto = new XcCourseTablesDto();
        if (!Objects.isNull(xcCourseTables)) {
            // 如果不为空，证明已经买了课，可以查看
            BeanUtils.copyProperties(xcCourseTables, xcCourseTablesDto);
            xcCourseTablesDto.setLearnStatus("702001");//可以学习
        } else {
            // 如果不为空，证明没买课，不能看
            xcCourseTablesDto.setLearnStatus("702002");// 不能学习
        }
        return xcCourseTablesDto;
    }
}
