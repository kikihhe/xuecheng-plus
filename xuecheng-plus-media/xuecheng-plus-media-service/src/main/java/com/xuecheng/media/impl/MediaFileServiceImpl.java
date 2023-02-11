package com.xuecheng.media.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.mapper.MediaFileMapper;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.GetObjectArgs;
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
import java.io.InputStream;
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
    private String bucket_file;
    @Value("${minio.bucket.videofiles}")
    private String bucket_video;

    @Autowired
    private MediaFileMapper mediaFileMapper;


    /**
     * 检查文件是否存在
     * @param md5
     * @return
     */
    @Override
    public RestResponse<Boolean> checkFile(String md5) {
        MediaFiles mediaFiles = mediaFileMapper.selectById(md5);
        // 1. 查看是否在数据库中存在
        if (Objects.isNull(mediaFiles)) {
            return RestResponse.success(false);
        }
        // 2. 查看是否在MinIO中存在
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                            .bucket(mediaFiles.getBucket())
                    .object(mediaFiles.getFilePath())
                    .build());
            if (Objects.isNull(inputStream)) {
                return RestResponse.success(false);
            }
        } catch (Exception e) {
            log.error("{}", e);
            return RestResponse.success(false);

        }
        return RestResponse.success(true);
    }


    @Override
    public RestResponse<Boolean> checkChunk(String fileMd5, int chunkIndex) {

        //得到分块文件所在目录
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        //分块文件的路径
        String chunkFilePath = chunkFileFolderPath + chunkIndex;

        //查询文件系统分块文件是否存在
        //查看是否在文件系统存在
        GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                .bucket(bucket_video)
                .object(chunkFilePath)
                .build();
        try {
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            if(Objects.isNull(inputStream)){
                //文件不存在
                return RestResponse.success(false);
            }
        }catch (Exception e){
            //文件不存在
            return RestResponse.success(false);
        }


        return RestResponse.success(true);
    }

//    @Override
//    public RestResponse checkChunk(String md5, int chunk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        // 拿到分块文件的存储目录
//        String chunkFileFolderPath = getChunkFileFolderPath(md5);
//        // 根据目录就可以拼接出文件的路径 = 存储目录+序号
//        String chunkPath = chunkFileFolderPath + chunk;
//
//
//
//        // 2. 查看是否在MinIO中存在
//        try {
//            GetObjectArgs build = GetObjectArgs.builder()
//                    .bucket(bucket_video)
//                    .object(chunkPath)
//                    .build();
//            InputStream inputStream = minioClient.getObject(build);
//            if (Objects.isNull(inputStream)) {
//                return RestResponse.success(false);
//            }
//        } catch (Exception e) {
//            return RestResponse.success(true);
//        }
//        return RestResponse.success(true);
//    }

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
        addMediaToMinIO(bytes, bucket_file, objectName);

        // 2. 插入数据库, 为防止事务失效，使用代理类的addMediaToDB方法。
        String fileMd5 = DigestUtils.md5DigestAsHex(bytes);
        MediaFileServiceImpl mediaFileService = (MediaFileServiceImpl) AopContext.currentProxy();
        MediaFiles mediaFiles = mediaFileService.addMediaToDB(companyId, fileMd5, dto, bucket_file, objectName);

        // 3. 封装数据返回
        UploadFileResultDto uploadFileResultDto = new UploadFileResultDto();
        BeanUtils.copyProperties(mediaFiles, uploadFileResultDto);

        return uploadFileResultDto;
    }
    /**
     * 上传分块
     * @param md5 上传的分块所属的文件
     * @param chunk 上传的分块的序号
     * @param bytes 上传的分块的字节内容
     * @return
     */
    @Override
    public RestResponse uploadChunk(String md5, int chunk, byte[] bytes) {
        // 获取文件应该放在哪个目录
        String chunkFileFolderPath = getChunkFileFolderPath(md5);
        // 获取分块的完整路径
        String chunkPath = chunkFileFolderPath + chunk;

        try {
            addMediaToMinIO(bytes, bucket_video, chunkPath);

        } catch (Exception e) {
            throw new RuntimeException("文件上传失败!");
        }

        return RestResponse.success();
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

    /**
     * 得到分块文件的目录,上传分块时按照md5进行分块，检查时也按照这个目录检查
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5) {
        return fileMd5.substring(0, 1) + "/" + fileMd5.substring(1, 2) + "/" + fileMd5 + "/" + "chunk" + "/";
    }


}
