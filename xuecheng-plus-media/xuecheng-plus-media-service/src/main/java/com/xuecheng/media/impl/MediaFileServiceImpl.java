package com.xuecheng.media.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.mapper.MediaFileMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:16
 */
@Service
public class MediaFileServiceImpl extends ServiceImpl<MediaFileMapper, MediaFiles> implements MediaFileService {

    @Autowired
    private MinioClient minioClient;
    @Value("${minio.bucket.files}")
    private String bucket;
    @Autowired
    private MediaFileMapper mediaFileMapper;


    /**
     * 查询本机构所有文件
     * @param companyId 本机构id
     * @param pageParams 分页参数: 当前页码、每页数据数
     * @param dto 查询条件
     * @return 返回结果
     */
    @Override
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto dto) {
        LambdaQueryWrapper<MediaFiles> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(MediaFiles::getFilename, dto.getFilename());
        queryWrapper.like(MediaFiles::getFilename, dto.getFilename());
        if (!Strings.isEmpty(dto.getFileType())) {
            queryWrapper.eq(MediaFiles::getFileType, dto.getFileType());
        }
        Page<MediaFiles> page = new Page<>(pageParams.getPageNo(), pageParams.getPageSize());
        mediaFileMapper.selectPage(page, queryWrapper);
        List<MediaFiles> records = page.getRecords();
        long total = page.getTotal();
        PageResult<MediaFiles> pageResult = new PageResult<>(records, total, pageParams.getPageNo(), pageParams.getPageSize());
        return pageResult;
    }



    /**
     * 上传文件
     * @param companyId 上传的公司的id
     * @param bytes 需要上传的文件
     * @param dto 文件的信息
     * @param folder 文件路径
     * @param objectName 文件名
     * @return
     */
    @Override
    public UploadFileResultDto uploadFile(Long companyId,
                                          byte[] bytes,
                                          UploadFileParamsDto dto,
                                          String folder,
                                          String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 1. 上传至minio
        // 如果未指定文件夹，以当前 年/月/日 作为文件夹目录
        if (StringUtils.isEmpty(folder)) {
            folder = getFolder(LocalDate.now());
        }
        // 如果没以 "/" 结尾，补上
        if (!folder.endsWith("/")) {
            folder += "/";
        }
        // 如果没指定文件名称, 生成一个以 "时:分:秒-UUID" 的文件名
        String filename = dto.getFilename();
        if (StringUtils.isEmpty(objectName)) {
            objectName = generateFileName(LocalDateTime.now()) + filename.substring(filename.lastIndexOf("."));
        }
        // 完整的文件名 = 文件目录 + 文件名
        objectName = folder + objectName;
        // 上传
        addMediaToMinIO(bytes, bucket, objectName);

        // 2. 插入数据库, 为防止事务失效，使用代理类的addMediaToDB方法。
        String fileMd5 = DigestUtils.md5DigestAsHex(bytes);
        MediaFileServiceImpl mediaFileService = (MediaFileServiceImpl) AopContext.currentProxy();
        MediaFiles mediaFiles = mediaFileService.addMediaToDB(companyId, fileMd5, dto, bucket, objectName);

        // 3. 封装数据返回
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

        return uploadFileResultDto;
    }

    /**
     * 将文件上传至MinIO
     * @param bytes 文件的字节数组
     * @param bucket 文件上传到哪个桶
     * @param objectName 文件全限定名称
     */
    public void addMediaToMinIO(byte[] bytes, String bucket, String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 文件类型默认为默认类型:application/octet-stream
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        if (objectName.contains(".")) {
            // 文件后缀名
            String extension = objectName.substring(objectName.lastIndexOf("."));
            // 根据文件后缀名拿到文件类型
            ContentInfo extensionMatch = ContentInfoUtil.findExtensionMatch(extension);
            // 如果是正确的文件类型，赋值给contentType。如果是离谱的，例如:.abc，pass掉。
            if (!Objects.isNull(extensionMatch)) {
                contentType = extensionMatch.getMimeType();
            }
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucket)
                .object(objectName)
                .contentType(contentType)
                .stream(inputStream, inputStream.available(), -1)
                .build());

    }

    /**
     * 将文件信息保存到数据库
     * @param companyId 上传人所属的公司/机构的id
     * @param fileId 文件的md5值
     * @param dto 文件信息
     * @param bucket 文件放在哪个桶
     * @param objectName 文件的全限定名
     * @return 返回文件的基本信息
     */
    @Transactional
    public MediaFiles addMediaToDB(Long companyId, String fileId,UploadFileParamsDto dto, String bucket, String objectName) {
        // 先看看数据库中有没有
        String fileMd5 = fileId;
        MediaFiles mediaFiles = mediaFileMapper.selectById(fileMd5);
        // 如果数据库中没有这个文件, 插入
        if (Objects.isNull(mediaFiles)) {
            // 封装数据
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(dto, mediaFiles);
            mediaFiles.setId(fileMd5);
            mediaFiles.setFileId(fileMd5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setFilename(dto.getFilename());
            mediaFiles.setBucket(bucket);
            mediaFiles.setFilePath(objectName);
            mediaFiles.setUrl("/" + bucket + "/" + objectName);
            mediaFiles.setCreateDate(LocalDateTime.now());
            mediaFiles.setStatus("1");
            mediaFiles.setAuditStatus("002003");
            int insert = mediaFileMapper.insert(mediaFiles);
            if (insert != 1) {
                throw new RuntimeException("文件插入数据库失败!");
            }
        } else {
            throw new RuntimeException("该文件已存在!无法重复上传");
        }
        return mediaFiles;
    }

    // 生成文件目录
    public String getFolder(LocalDate localDate) {
        String[] split = localDate.toString().split("-");
        // 如: 2023/2/9
        return split[0] + "/" + split[1] + "/" + split[2] + "/";
    }

    // 生成给文件名
    public String generateFileName(LocalDateTime localDateTime) {
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        String substring = UUID.randomUUID().toString().substring(0, 7);
        return hour + ":" + minute + ":" + second + ":" + substring;

    }


}
