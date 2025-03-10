package com.computer.bikeSupervision.controller;


import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


@Slf4j
@RestController
@CrossOrigin
@Api(tags = "阿里云云端保存接口")
@RequestMapping("/Upload")
public class UploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @ApiOperation(value = "上传图片至云端接口", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/uploadImage")
    public Result<String> uploadImage(MultipartFile image) throws Exception {
        log.info("文件上传，文件名{}", image.getOriginalFilename());

        //调用阿里云OSS工具类进行上传
        String url = aliOSSUtils.upload(image);
        log.info("文件上传完成，文件访问路径url：{}", url);

        return Result.ok(url);
    }

    @ApiOperation(value = "上传文件至云端接口", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/uploadFile")
    public Result<String> uploadFile(MultipartFile file) throws Exception {
        log.info("文件上传，文件名{}", file.getOriginalFilename());

        //调用阿里云OSS工具类进行上传
        String url = aliOSSUtils.upload(file);
        log.info("文件上传完成，文件访问路径url：{}", url);

        return Result.ok(url);
    }

    @ApiOperation(value = "上传头像至云端接口", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/uploadHead")
    public Result<String> uploadHead(MultipartFile head) throws Exception {
        log.info("文件上传，文件名{}", head.getOriginalFilename());

        //调用阿里云OSS工具类进行上传
        String url = aliOSSUtils.upload(head);
        log.info("文件上传完成，文件访问路径url：{}", url);

        return Result.ok(url);
    }

    @ApiOperation(value = "上传图片至云端接口", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/uploadTwoFile")
    public Result<Map<String, String>> uploadTwoFile(MultipartFile image, MultipartFile file) throws Exception {
        log.info("文件上传，文件名{}", image.getOriginalFilename());

        //调用阿里云OSS工具类进行上传
        Map<String, String> stringStringMap = aliOSSUtils.uploadTwoFile(image, file);
        log.info("文件上传完成，文件访问路径url：{}", stringStringMap);

        return Result.success(stringStringMap);
    }
}
