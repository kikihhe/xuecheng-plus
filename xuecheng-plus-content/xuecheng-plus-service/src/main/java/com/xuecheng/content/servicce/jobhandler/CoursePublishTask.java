package com.xuecheng.content.servicce.jobhandler;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xuecheng.content.servicce.CoursePublishService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * 课程发布任务
 */
@Slf4j
@Component
public class CoursePublishTask extends MessageProcessAbstract {




    @XxlJob("CoursePublishJobHandler")
    public void CoursePublishJobHandler() {
        // 分片序号
        int shardIndex = XxlJobHelper.getShardIndex();
        // 分片总数
        int shardTotal = XxlJobHelper.getShardTotal();

        log.debug("shardIndex: {}, shardIndex: {}", shardIndex, shardTotal);

        // 参数: 分片序号 分片总数 消息类型 每次执行的最多消息数 执行一次任务调度最长等待时间
        process(shardIndex, shardTotal, "course_publish", 5, 60);
    }

    @Autowired
    private CoursePublishService coursePublishService;



    /**
     * 发布任务
     * @param mqMessage 执行任务内容
     * @return
     */
    @Override
    public boolean execute(MqMessage mqMessage) {
        log.info("开始执行发布任务, 课程id: {}", mqMessage.getBusinessKey1());

        // 将课程信息静态化
        generateCourseHtml(mqMessage, Long.parseLong(mqMessage.getBusinessKey1()));

        // 将课程信息存储到es

        // 将课程信息存储到redis



        return true;
    }

    public void generateCourseHtml(MqMessage mqMessage, Long courseId) {
        // 先判断任务是否已经完成
        int stageOne = this.getMqMessageService().getStageOne(courseId);
        if (stageOne > 0) {
            log.debug("上传静态页面任务已完成, 无需重复");
            return ;
        }

        // 生成静态页面
        File file = coursePublishService.generateCourseHtml(courseId);

        if (Objects.isNull(file)) {
            throw new RuntimeException("课程静态化失败");
        }


        // 将页面上传至MinIO
        coursePublishService.uploadCourseHtml(courseId, file);

        // 完成任务后，将相应字段更新
        getMqMessageService().completedStageOne(courseId);


    }
}
