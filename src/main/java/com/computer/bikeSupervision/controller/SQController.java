package com.computer.bikeSupervision.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.vo.PlatePassSQVo;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.CameraUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;

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

    @Autowired
    CameraUtils cameraUtils;

    @ApiOperation(value = "二维码生成", notes = "需要传递一个特定的车牌号")
    @PostMapping("/generateSqCode")
    public Result<String> generateSqCode(String plateNumber) throws Exception {
        //获取当前登陆人的id
        Long id = BaseContext.getCurrentId();
        //生成二维码 获取云端存储的url路径并返回给前端
        String url = platePassService.generateSqCode(id,plateNumber);

        log.info("上传成功，url为：{}", url);
        return Result.success(url);
    }


    @ApiOperation(value = "二维码解析", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/parseSqCode")
    public Result<String> parseSqCode(MultipartFile image) throws Exception {
        //解析二维码
        String content = qrCodeGenerator.parseQRCodeData(image);
        log.info("解析成功，解析的内容为：{}", content);

        PlatePassSQVo platePassSQVo = JSONObject.parseObject(content, PlatePassSQVo.class);
        // 与数据库比对
        boolean isMatch = compareWithDatabase(platePassSQVo);
        if (isMatch) {
            return Result.success("二维码内容匹配成功");
        } else {
            return Result.error("二维码内容匹配失败");
        }
    }

    @ApiOperation(value = "摄像头捕获二维码解析", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/scanQrCode")
    public Result<String> scanQrCode() {
        try {
            // 从摄像头捕获图像
            BufferedImage image = cameraUtils.captureImageFromCamera();
            // 调用新增的解析方法解析图像中的二维码内容
            String qrCodeContent = qrCodeGenerator.parseQRCodeFromBufferedImage(image);
            // 解析 JSON 数据
            PlatePassSQVo platePass = JSONObject.parseObject(qrCodeContent, PlatePassSQVo.class);

            // 与数据库比对
            boolean isMatch = compareWithDatabase(platePass);
            if (isMatch) {
                return Result.success("二维码内容匹配成功");
            } else {
                return Result.error("二维码内容匹配失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("二维码扫描失败");
        }
    }

    @ApiOperation(value = "接受并比对前端识别出来的二维码信息")
    @PostMapping("/compareWithDatabase")
    public Result<String> compareWithDatabase(String qrCodeContent) {
        // 解析 JSON 数据
        PlatePassSQVo platePass = JSONObject.parseObject(qrCodeContent, PlatePassSQVo.class);

        // 与数据库比对
        boolean isMatch = compareWithDatabase(platePass);
        if (isMatch) {
            return Result.success("二维码内容匹配成功");
        } else {
            return Result.error("二维码内容匹配失败");
        }
    }


    /**
     * 与数据库比对
     */
    private boolean compareWithDatabase(PlatePassSQVo platePass) {

        String studentNumber = platePass.getStudentNumber();
        String plateNumber = platePass.getPlateNumber();
        String passNumber = platePass.getPassNumber();

        LambdaQueryWrapper<PlatePass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlatePass::getStudentNumber, studentNumber)
                .eq(PlatePass::getPlateNumber, plateNumber)
                .eq(PlatePass::getPassNumber, passNumber);
        // 根据 studentNumber、plateNumber 和 passNumber 查询 plate_pass 表
        PlatePass platePassOne = platePassService.getOne(lambdaQueryWrapper);

        return platePassOne != null;
    }
}

