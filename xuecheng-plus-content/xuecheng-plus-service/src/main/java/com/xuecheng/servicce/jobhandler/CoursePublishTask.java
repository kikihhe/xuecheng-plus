package com.xuecheng.servicce.jobhandler;

import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MessageProcessAbstract;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

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



    /**
     * 发布任务
     * @param mqMessage 执行任务内容
     * @return
     */
    @Override
    public boolean execute(MqMessage mqMessage) {
        log.info("开始执行发布任务, 课程id: {}", mqMessage.getBusinessKey1());


        return true;
    }
}
