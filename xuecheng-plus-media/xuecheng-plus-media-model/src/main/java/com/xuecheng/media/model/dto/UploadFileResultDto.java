package com.xuecheng.media.model.dto;

import com.xuecheng.media.model.po.MediaFiles;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author : 小何
 * @Description :
 * @date : 2023-02-09 18:41
 */
@Data
@ApiModel(value = "上传文件接口的返回类型")
public class UploadFileResultDto extends MediaFiles {
}
