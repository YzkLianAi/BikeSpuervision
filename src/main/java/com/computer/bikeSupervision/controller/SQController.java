package com.computer.bikeSupervision.controller;



import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "二维码生成与解析")
@RequestMapping("/SQ")
public class SQController {

    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    /*@PostMapping("/{id}")
    public Result<String> test(@PathVariable long id) throws Exception {
        //根据id进行查询个人数据
        User user = sqService.getUserById(id);
        //将拿到的数据转换成字符串 转 json封装
        String json = JSONObject.toJSONString(user);
        log.info("json数据为：{}", json);
        //将此部分数据作为内容 用于生成二维码


        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        String url = aliOSSUtils.upload(image);
        log.info("上传成功，url为：{}", url);
        return Result.success(url);
    }

    @PostMapping("/jiexi")
    public Result<User> jiexi(MultipartFile image) throws Exception {

        String content = qrCodeGenerator.parseQRCodeData(image);
        log.info("解析成功，url为：{}", content);
        User user = JSONObject.parseObject(content, User.class);
        log.info("user为：{}", user);
        return Result.success(user);
    }*/


}
