package com.xuecheng.media.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:13
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
public class MediaFilesController {
    @Value("${minio.bucket.files}")
    private String bucket;

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MinioClient minioClient;




    @ApiOperation("查询所有文件")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams,@RequestBody QueryMediaParamsDto dto) {


        return null;
    }


    /**
     * 上传文件
     * @param filedata 需要上传的文件
     * @param folder 上传文件的目录
     * @param objectName 上传文件的名字
     * @return
     */
    @PostMapping(value = "/upload/coursefile", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public UploadFileResultDto upload(@RequestPart("filedata") MultipartFile filedata,
                                      @RequestParam("folder") String folder,
                                      @RequestParam("objectName") String objectName) throws IOException {
        Long companyId = 117L;

//        mediaFileService.uploadFile(companyId, filedata.getBytes(), )
        return null;


    }

}
