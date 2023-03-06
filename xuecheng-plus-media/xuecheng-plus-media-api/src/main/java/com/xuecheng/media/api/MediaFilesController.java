package com.xuecheng.media.api;

import com.xuecheng.base.model.PageParams;
import com.xuecheng.base.model.PageResult;
import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.QueryMediaParamsDto;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.model.dto.UploadFileResultDto;
import com.xuecheng.media.model.po.MediaFiles;
import com.xuecheng.media.service.MediaFileService;
import io.minio.MinioClient;
import io.minio.errors.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:13
 */
@Api(value = "媒资文件管理接口", tags = "媒资文件管理接口")
@RestController
@Slf4j
public class MediaFilesController {
    @Value("${minio.bucket.files}")
    private String bucket;

    @Autowired
    private MediaFileService mediaFileService;

    @Autowired
    private MinioClient minioClient;


    /**
     * 显示本机构所有文件列表
     * @param pageParams 分页参数
     * @param queryMediaParamsDto 查询信息
     */
    @ApiOperation("媒资列表查询接口")
    @PostMapping("/files")
    public PageResult<MediaFiles> list(PageParams pageParams, @RequestBody QueryMediaParamsDto queryMediaParamsDto) {
        Long companyId = 1232141425L;
        return mediaFileService.queryMediaFiles(companyId, pageParams, queryMediaParamsDto);
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
                                      @RequestParam(value = "folder", required = false) String folder,
                                      @RequestParam(value = "objectName", required = false) String objectName) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Long companyId = 1232141425L;

        UploadFileParamsDto dto = new UploadFileParamsDto();
        String contentType = filedata.getContentType();
        dto.setContentType(contentType); // 文件类型
        dto.setFileSize(filedata.getSize()); // 文件大小
        if (contentType.contains("image")) {
            // 如果文件类型中含有 "image" 字符串，证明文件类型为图片
            dto.setFileType("001001");
        } else {
            // 否则文件类型为其他
            dto.setFileType("001003");
        }

        // 如果传入时指定名称，使用指定名称，如果传入时未指定名称，使用文件本身名称
        // 如果二者都为空，生成名称
        String fileName = !StringUtils.isEmpty(objectName) ? objectName : filedata.getOriginalFilename();
        dto.setFilename(fileName);
        if (StringUtils.isEmpty(objectName)) {
            objectName = fileName;
        }

        return mediaFileService.uploadFile(companyId, filedata.getBytes(), dto, folder, objectName);

    }


    /**
     * 预览文件
     * @param mediaId 文件id
     * @return 返回该文件在MinIO中的路径
     */
    @ApiOperation("文件预览")
    @GetMapping("/preview/{mediaId}")
    public RestResponse<String> getPlayUrlByMediaId(@PathVariable String mediaId) {
        if (StringUtils.isEmpty(mediaId)) {
            throw new RuntimeException("文件id为空，你想看哪个文件?");
        }
        MediaFiles mediaFile = mediaFileService.getById(mediaId);
        if (Objects.isNull(mediaFile)) {
            log.error("查看id为 {} 的文件不存在!", mediaId);
            throw new RuntimeException("文件不存在!");
        }
        String url = mediaFile.getUrl();
        if (StringUtils.isEmpty(url)) {
            log.error("预览 文件id为 {} , url为 {} 的文件失败", mediaId, url);
            throw new RuntimeException("该文件不支持预览!");
        }

        return RestResponse.success(url);
    }
}
