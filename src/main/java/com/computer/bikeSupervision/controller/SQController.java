package com.computer.bikeSupervision.controller;



import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "二维码生成与解析")
@RequestMapping("/SQ")
public class SQController {
    @Resource
    StudentsService studentsService;

    @Resource
    private AliOSSUtils aliOSSUtils;

    @Resource
    private QRCodeGenerator qrCodeGenerator;


    @ApiOperation(value = "二维码生成", notes = "无需传递参数")
    @PostMapping("/generateSqCode")
    public Result<String> generateSqCode() throws Exception {
        //获取当前登陆人的id
        Long id = BaseContext.getCurrentId();

        //将拿到的数据转换成字符串 转 json封装

        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Students::getId, id);

        Students student = studentsService.getOne(queryWrapper);
        /*if (student.getPlateNumber() == null){
            throw new CustomException("请先绑定车牌号");
        }

        if (student.getPassNumber() == null){
            throw new CustomException("请先绑定通行证号");
        }*/

        StudentSQVo studentSQVo = new StudentSQVo();
        //第一个参数是原始数据 第二参数 为 拷贝的对象目标
        BeanUtils.copyProperties(student, studentSQVo);

        log.info("拷贝好的属性：{}", studentSQVo);
        //将实体类转换成json格式的数据 用于生成二维码
        String json = JSONObject.toJSONString(studentSQVo);

        //将此部分数据作为内容 用于生成二维码
        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        String url = aliOSSUtils.upload(image);
        log.info("上传成功，url为：{}", url);
        return Result.success(url);
    }


    @ApiOperation(value = "二维码解析", notes = "需要传递一个MultipartFile 类型的文件")
    @PostMapping("/parseSqCode")
    public Result<StudentSQVo> parseSqCode(MultipartFile image) throws Exception {

        String content = qrCodeGenerator.parseQRCodeData(image);
        log.info("解析成功，解析的内容为：{}", content);

        StudentSQVo studentSQVo = JSONObject.parseObject(content, StudentSQVo.class);
        //将解析的json格式的数据再转换成实体类 返回给前端做展示 在 @RestController下会自动将实体类封装成json
        return Result.success(studentSQVo);
    }


}
