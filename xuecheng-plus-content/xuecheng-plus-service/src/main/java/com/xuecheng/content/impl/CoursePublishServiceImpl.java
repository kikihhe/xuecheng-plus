package com.xuecheng.content.impl;
import com.xuecheng.content.config.MultipartSupportConfig;
import com.xuecheng.content.feignclient.MediaServiceClient;
import com.xuecheng.content.mapper.CourseBaseMapper;
import com.xuecheng.content.mapper.CourseMarketMapper;
import com.xuecheng.content.mapper.CoursePublishMapper;
import com.xuecheng.content.mapper.CoursePublishPreMapper;
import com.xuecheng.content.servicce.CourseBaseService;
import com.xuecheng.messagesdk.mapper.MqMessageMapper;
import com.xuecheng.messagesdk.service.MqMessageHistoryService;
import com.xuecheng.messagesdk.service.impl.MqMessageServiceImpl;
import freemarker.template.Configuration;
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
import com.xuecheng.messagesdk.model.po.MqMessage;
import com.xuecheng.messagesdk.service.MqMessageService;
import com.xuecheng.content.servicce.CoursePublishService;
import com.xuecheng.content.servicce.TeachplanService;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-20 16:35
 */
@Slf4j
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



    @Autowired
    private MqMessageService mqMessageService;

    @Autowired
    private MediaServiceClient mediaServiceClient;

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
        saveCoursePublishMessage(courseId);

        // 3. 将课程信息从预发布表中删除
        int i = deleteCoursePublishPre(courseId);
        if (i <= 0) {
            throw new RuntimeException("课程预发布信息删除失败");
        }
    }

    /**
     * 生成静态页面
     * @param courseId  课程id
     * @return
     */
    @Override
    public File generateCourseHtml(Long courseId) {

        try {
            //配置freemarker
            Configuration configuration = new Configuration(Configuration.getVersion());

            //加载模板
            //选指定模板路径,classpath下templates下
            //得到classpath路径
            String classpath = this.getClass().getResource("/").getPath();
            configuration.setDirectoryForTemplateLoading(new File(classpath + "/templates/"));
            //设置字符编码
            configuration.setDefaultEncoding("utf-8");

            //指定模板文件名称
            Template template = configuration.getTemplate("course_template.ftl");

            //准备数据
            CoursePreviewDto coursePreviewInfo = getCoursePreviewInfo(courseId);

            Map<String, Object> map = new HashMap<>();
            map.put("model", coursePreviewInfo);

            //静态化
            //参数1：模板，参数2：数据模型
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
            System.out.println(content);
            //将静态化内容输出到文件中
            InputStream inputStream = IOUtils.toInputStream(content);
            //输出流
            File course = File.createTempFile("course", ".html");
            FileOutputStream outputStream = new FileOutputStream(course);
            IOUtils.copy(inputStream, outputStream);
            return course;
        } catch (Exception e) {
            log.debug("课程静态化异常:{}， {}",e.getMessage(),e);
            return null;
        }
    }

    /**
     * 将静态页面上传至MinIO
     * @param courseId
     * @param file  静态化文件
     */
    @Override
    public void uploadCourseHtml(Long courseId, File file) {
        MultipartFile multipartFile = MultipartSupportConfig.getMultipartFile(file);
        String course = mediaServiceClient.upload(multipartFile, "course", courseId + ".html");
        // 远程调用失败
        if (StringUtils.isEmpty(course)) {
            throw new RuntimeException("远程调用上传文件失败, courseid: " + courseId);
        }
    }

    // 将课程信息从预发布表插入到发布表
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
    // 将课程信息插入到mq_message表
    private void saveCoursePublishMessage(Long courseId) {
        MqMessage course_publish = mqMessageService.addMessage("course_publish", String.valueOf(courseId), null, null);
        if (Objects.isNull(course_publish)) {
            throw new RuntimeException("将课程信息插入到mq_message表失败");
        }

    }
    private int deleteCoursePublishPre(Long courseId) {
        int i = coursePublishPreMapper.deleteById(courseId);
        return i;
    }
}
