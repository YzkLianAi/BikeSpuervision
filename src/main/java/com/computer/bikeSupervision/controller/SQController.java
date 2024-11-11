package com.computer.bikeSupervision.controller;



import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@CrossOrigin
@Api(tags = "二维码生成与解析")
@RequestMapping("/SQ")
public class SQController {
    @Autowired
    StudentsService studentsService;

    @Autowired
    private AliOSSUtils aliOSSUtils;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;


    @PostMapping("/StudentSQ")
    public Result<String> test() throws Exception {
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
        String json = JSONObject.toJSONString(studentSQVo);

        //log.info("json数据为：{}", json);
        //将此部分数据作为内容 用于生成二维码

        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        String url = aliOSSUtils.upload(image);
        log.info("上传成功，url为：{}", url);
        return Result.success(url);
    }


    /*
    @PostMapping("/jiexi")
    public Result<User> jiexi(MultipartFile image) throws Exception {

        String content = qrCodeGenerator.parseQRCodeData(image);
        log.info("解析成功，url为：{}", content);
        User user = JSONObject.parseObject(content, User.class);
        log.info("user为：{}", user);
        return Result.success(user);
    }*/


}
