package com.xuecheng.media;

import io.minio.*;
import io.minio.errors.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;

/**
 * @author Mr.M
 * @version 1.0
 * @description 测试minio上传文件、删除文件、查询文件
 * @date 2022/10/13 14:42
 */
public class MinIOTest {
    // 创建minio客户端
    static MinioClient minioClient = MinioClient.builder()
            .endpoint("http://192.168.101.65:9000")
            .credentials("minioadmin", "minioadmin")
            .build();

//    上传文件
    @Test
    public void testUpload() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        boolean test = minioClient.bucketExists(BucketExistsArgs.builder().bucket("test").build());
        System.out.println(test);

        try {
            ObjectWriteResponse test1 = minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("test")
                    .filename("C:\\Users\\23825\\Desktop\\备忘录.md")
                    .object("备忘录.md")
                    .build());
            System.out.println("上传成功, 结果: " + test1);

        } catch(Exception e) {
            e.printStackTrace();
        }


    }

    //        上传目录
    @Test
    public void testUploadFile() {
        String file = LocalDate.now().toString();

//        System.out.println(file);
        try {
            ObjectWriteResponse test1 = minioClient.uploadObject(UploadObjectArgs.builder()
                    .bucket("test")
                    .filename("C:\\Users\\23825\\Desktop\\备忘录.md")
                    .object(file + "/备忘录.md")
                    .build());
            System.out.println("上传成功, 结果: " + test1);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // 删除一个已经上传的文件
    @Test
    public void testRemove() {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket("test")
                    .object("备忘录.md")
                    .build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
