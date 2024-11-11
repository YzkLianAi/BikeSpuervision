package com.computer.bikeSupervision.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Component
public class QiniuCloudUtils {


    @Value("${qiniu.accessKey}")
    private String accessKey;


    @Value("${qiniu.secretKey}")
    private String secretKey;


    @Value("${qiniu.bucketName}")
    private String bucketName;


    @Value("${qiniu.domain}")
    private String domain;


    // 生成上传凭证
    private String getUploadToken() {
        Auth auth = Auth.create(accessKey, secretKey);
        return auth.uploadToken(bucketName);
    }

    // 上传图片到七牛云
    public String uploadImage(MultipartFile multipartFile) throws IOException {
        // 配置七牛云存储区域
        Configuration cfg = new Configuration(Region.autoRegion());
        UploadManager uploadManager = new UploadManager(cfg);

        // 获取文件的字节数组
        byte[] bytes = multipartFile.getBytes();

        // 生成上传凭证
        String uploadToken = getUploadToken();

        //将文件名用UUID修改后上传
        String originalFilename = multipartFile.getOriginalFilename();
        String fileName = null;

        if (originalFilename != null) {
            fileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        // 上传文件并获取响应 用 uuid 作为文件名
        Response response = uploadManager.put(bytes, fileName, uploadToken);

        // 判断上传是否成功
        if (response.isOK()) {
            // 返回文件在七牛云存储的URL
            return "http://" + domain + "/" + fileName;
        } else {
            throw new QiniuException(response);
        }
    }
}