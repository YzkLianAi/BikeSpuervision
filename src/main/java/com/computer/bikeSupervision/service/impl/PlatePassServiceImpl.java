package com.computer.bikeSupervision.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.mapper.PlatePassMapper;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import com.computer.bikeSupervision.utils.QiniuCloudUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/*
 * 车牌通行证管理服务实现类
 */
@Slf4j
@Service
public class PlatePassServiceImpl extends ServiceImpl<PlatePassMapper, PlatePass>
        implements PlatePassService {

    @Autowired
    StudentsMapper studentsMapper;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private QiniuCloudUtils qiniuCloudUtils;

    public String generateSqCode(Long id) throws Exception {
        // 根据id查询当前人的信息
        Students student = studentsMapper.selectById(id);

        //获取该学生对应的 学号 和 学校 用来在 车牌 通行证管理表中做查询
        String schoolName = student.getSchoolName();
        String studentNumber = student.getStudentNumber();
        String studentName = student.getStudentName();

        LambdaQueryWrapper<PlatePass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlatePass::getSchoolName, schoolName).
                eq(PlatePass::getStudentNumber, studentNumber);

        //查询 该学生 是否拥有 车牌或者通行证
        //TODO 查询逻辑问题 无法处理一个学生对应多个车牌和通行证的情况
        PlatePass one = this.getOne(lambdaQueryWrapper);

        StudentSQVo studentSQVo = new StudentSQVo();
        studentSQVo.setStudentName(studentName);
        // 第一个参数是原始数据 第二参数 为 拷贝的对象目标

        BeanUtils.copyProperties(one, studentSQVo);

        log.info("拷贝好的属性：{}", studentSQVo);
        // 将实体类转换成json格式的数据 用于生成二维码
        String json = JSONObject.toJSONString(studentSQVo);

        // 将此部分数据作为内容 用于生成二维码图片
        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        // 将二维码上传到七牛云
        String url = qiniuCloudUtils.uploadImage(image);

        // 将url保存到学生的二维码云端路径字段当中
        one.setQrCode(url);
        // 更新学生信息
        this.updateById(one);
        // 返回url路径
        return url;
    }
}




