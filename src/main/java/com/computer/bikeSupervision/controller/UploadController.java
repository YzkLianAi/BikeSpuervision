package com.computer.bikeSupervision.controller;


import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@CrossOrigin
@Api(tags = "阿里云云端保存接口")
public class UploadController {
    @Autowired
    private AliOSSUtils aliOSSUtils;

    @ApiOperation(value = "上传图片至云端接口", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile image) throws Exception {
        log.info("文件上传，文件名{}", image.getOriginalFilename());

        //调用阿里云OSS工具类进行上传
        String url = aliOSSUtils.upload(image);
        log.info("文件上传完成，文件访问路径url：{}", url);

        return Result.success(url);
    }
}
