package com.computer.bikeSupervision.controller;


import com.alibaba.fastjson.JSONObject;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "二维码生成与解析")
@RequestMapping("/SQ")
public class SQController {
    @Autowired
    PlatePassService platePassService;

    @Autowired
    StudentsService studentsService;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @ApiOperation(value = "二维码生成", notes = "无需传递参数")
    @PostMapping("/generateSqCode")
    public Result<String> generateSqCode() throws Exception {
        //获取当前登陆人的id
        Long id = BaseContext.getCurrentId();
        //生成二维码 获取云端存储的url路径并返回给前端
        String url = platePassService.generateSqCode(id);

        log.info("上传成功，url为：{}", url);
        return Result.success(url);
    }


    @ApiOperation(value = "二维码解析", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/parseSqCode")
    public Result<StudentSQVo> parseSqCode(MultipartFile image) throws Exception {
        //解析二维码
        String content = qrCodeGenerator.parseQRCodeData(image);
        log.info("解析成功，解析的内容为：{}", content);

        StudentSQVo studentSQVo = JSONObject.parseObject(content, StudentSQVo.class);
        //将解析的json格式的数据再转换成实体类 返回给前端做展示 在 @RestController下会自动将实体类封装成json
        return Result.success(studentSQVo);
    }
}
