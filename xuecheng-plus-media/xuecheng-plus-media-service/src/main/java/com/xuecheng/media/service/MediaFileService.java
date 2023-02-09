package com.xuecheng.media.service;

import com.baomidou.mybatisplus.extension.service.IService;
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

}
