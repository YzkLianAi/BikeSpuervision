package com.computer.bikeSupervision.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import com.computer.bikeSupervision.utils.QiniuCloudUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private QiniuCloudUtils qiniuCloudUtils;

    @Override
    public String generateSqCode(Long id) throws Exception {
        //根据id查询当前人的信息
        Students student = this.getById(id);
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
        String url = qiniuCloudUtils.uploadImage(image);

        //将url保存到学生的二维码云端路径字段当中
        student.setQrCode(url);
        this.updateById(student);

        return url;
    }
}




