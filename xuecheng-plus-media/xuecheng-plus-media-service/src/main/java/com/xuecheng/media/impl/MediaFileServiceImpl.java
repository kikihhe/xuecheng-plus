package com.xuecheng.media.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xuecheng.media.mapper.MediaFileMapper;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        objectName = folder + objectName;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        String contentType = dto.getContentType();

        minioClient.putObject(PutObjectArgs.builder()
                        .bucket(bucket)
                        .object(objectName)
                        .contentType(contentType)
                        .stream(inputStream, inputStream.available(), -1)
                        .build());

        // 2. 插入数据库
        // 先看看数据库中有没有
        String fileMd5 = DigestUtils.md5DigestAsHex(bytes);
        MediaFiles mediaFiles = mediaFileMapper.selectById(fileMd5);
        // 如果数据库中没有这个文件, 插入
        if (Objects.isNull(mediaFiles)) {
            // 封装数据
            mediaFiles = new MediaFiles();
            BeanUtils.copyProperties(dto, mediaFiles);
            mediaFiles.setId(fileMd5);
            mediaFiles.setFileId(fileMd5);
            mediaFiles.setCompanyId(companyId);
            mediaFiles.setFilename(filename);
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
        }
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

        return uploadFileResultDto;
    }

    public String getFolder(LocalDate localDate) {
        String[] split = localDate.toString().split("-");
        // 如: 2023/2/9
        return split[0] + "/" + split[1] + "/" + split[2] + "/";
    }

    public String generateFileName(LocalDateTime localDateTime) {
        int hour = localDateTime.getHour();
        int minute = localDateTime.getMinute();
        int second = localDateTime.getSecond();
        String substring = UUID.randomUUID().toString().substring(0, 7);
        return hour + ":" + minute + ":" + second + ":" + substring;

    }


}
