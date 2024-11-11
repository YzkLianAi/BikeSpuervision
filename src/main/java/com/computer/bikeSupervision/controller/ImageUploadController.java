package com.computer.bikeSupervision.controller;


import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.QiniuCloudUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "七牛云云端保存接口")
public class ImageUploadController {

    @Autowired
    private QiniuCloudUtils qiniuCloudUtils;


    @PostMapping("/uploadImage")
    public Result<String> uploadImage(MultipartFile image) {
        try {
            String url = qiniuCloudUtils.uploadImage(image);
            log.info("图片上传成功，url:{}", url);
            return Result.success(url);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("图片上传失败");
        }
    }
}
