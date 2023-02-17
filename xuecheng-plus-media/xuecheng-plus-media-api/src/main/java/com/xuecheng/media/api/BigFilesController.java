package com.xuecheng.media.api;

import com.xuecheng.base.model.RestResponse;
import com.xuecheng.media.model.dto.UploadFileParamsDto;
import com.xuecheng.media.service.MediaFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 大文件上传接口
 */
@Slf4j
@Api(value = "大文件上传的接口", tags = "大文件上上传接口")
@RestController
public class BigFilesController {

    @Autowired
    private MediaFileService mediaFileService;

    /**
     * 文件上传前检查文件，检查它是否存在
     * @param fileMd5 文件的md5值，为数据库中文件的主键
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传前检查文件")
    @PostMapping("/upload/checkfile")
    public RestResponse<Boolean> checkFile(@RequestParam("fileMd5") String fileMd5) throws Exception {
        return mediaFileService.checkFile(fileMd5);
    }

    @ApiOperation(value = "分块文件上传前的检测")
    @PostMapping("/upload/checkchunk")
    public RestResponse<Boolean> checkChunk(@RequestParam("fileMd5") String fileMd5,
                                            @RequestParam("chunk") int chunk) throws Exception {
        return mediaFileService.checkChunk(fileMd5, chunk);
    }

    /**
     * 上传文件分块
     * @param file 需要上传的文件的分块
     * @param fileMd5 上传的分块属于哪个问及那
     * @param chunk 上传的分块的序号
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传分块文件")
    @PostMapping("/upload/uploadchunk")
    public RestResponse uploadchunk(@RequestParam("file") MultipartFile file,
                                    @RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("chunk") int chunk) throws Exception {
        return mediaFileService.uploadChunk(fileMd5,chunk,file.getBytes());

    }


    /**
     * 合并文件
     * @param fileMd5 需要合并的文件的md5值
     * @param fileName 文件名称，用于插入
     * @param chunkTotal 文件总共分了多少块，用于检查是否上传完毕
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "合并文件")
    @PostMapping("/upload/mergechunks")
    public RestResponse mergechunks(@RequestParam("fileMd5") String fileMd5,
                                    @RequestParam("fileName") String fileName,
                                    @RequestParam("chunkTotal") int chunkTotal) throws Exception {

        Long companyId = 1232141425L;
        UploadFileParamsDto dto = new UploadFileParamsDto();
        dto.setFilename(fileName);
        dto.setFileType("001002");
        dto.setTags("课程视频");
        return mediaFileService.mergeChunks(companyId, fileMd5, chunkTotal, dto);

    }



}
