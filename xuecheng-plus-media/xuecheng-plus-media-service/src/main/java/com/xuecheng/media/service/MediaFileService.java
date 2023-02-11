package com.xuecheng.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:16
 */
public interface MediaFileService extends IService<MediaFiles> {
    /**
     * 检查需要上传的文件
     * @param md5 文件的md5
     * @return
     */
    public RestResponse checkFile(String md5);

    /**
     * 检查分块是否已经下载
     * @param md5
     * @param chunk
     * @return
     */
    public RestResponse checkChunk(String md5, int chunk) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    /**
     * 分页条件查询文件，显示.
     * @param companyId
     * @param pageParams
     * @param dto
     * @return
     */
    public PageResult<MediaFiles> queryMediaFiles(Long companyId, PageParams pageParams, QueryMediaParamsDto dto);

    /**
     * 上传文件
     * @param companyId 上传的公司的id
     * @param bytes 需要上传的文件
     * @param dto 文件的信息
     * @param folder 文件路径
     * @param objectName 文件名
     * @return
     */
    public UploadFileResultDto uploadFile(Long companyId,
                                          byte[] bytes,
                                          UploadFileParamsDto dto,
                                          String folder,
                                          String objectName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;


    /**
     * 上传分块
     * @param md5 上传的分块所属的文件
     * @param chunk 上传的分块的序号
     * @param bytes 上传的分块的字节内容
     * @return
     */
    public RestResponse uploadChunk(String md5, int chunk, byte[] bytes);

}
