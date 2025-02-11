package com.computer.bikeSupervision.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.mapper.PlatePassMapper;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.PlatePassSQVo;
import com.computer.bikeSupervision.pojo.vo.StudentPlatePassVo;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.utils.AliOSSUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import com.computer.bikeSupervision.utils.QiniuCloudUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/*
 * 车牌通行证管理服务实现类
 */
@Slf4j
@Service
public class PlatePassServiceImpl extends ServiceImpl<PlatePassMapper, PlatePass>
        implements PlatePassService {

    @Autowired
    private StudentsMapper studentsMapper;

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private QiniuCloudUtils qiniuCloudUtils;

    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * 生成二维码
     */
    public String generateSqCode(Long id) throws Exception {
        // 根据id查询当前人的信息
        Students student = studentsMapper.selectById(id);

        //获取该学生对应的 学号 和 学校 用来在 车牌 通行证管理表中做查询
        String schoolName = student.getSchoolName();
        String studentNumber = student.getStudentNumber();

        LambdaQueryWrapper<PlatePass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(PlatePass::getSchoolName, schoolName).
                eq(PlatePass::getStudentNumber, studentNumber);

        //查询 该学生 是否拥有 车牌或者通行证
        //TODO 查询逻辑问题 无法处理一个学生对应多个车牌和通行证的情况 -> 解决方案 前端给定一个所需要查询的车牌号二维码信息
        PlatePass platePass = this.getOne(lambdaQueryWrapper);

        //StudentSQVo studentSQVo = new StudentSQVo();
        PlatePassSQVo platePassSQVo = new PlatePassSQVo();
        //studentSQVo.setStudentName(studentName);

        // 第一个参数是原始数据 第二参数 为 拷贝的对象目标
        BeanUtils.copyProperties(platePass, platePassSQVo);

        log.info("拷贝好的属性：{}", platePassSQVo);
        // 将实体类转换成json格式的数据 用于生成二维码
        String json = JSONObject.toJSONString(platePassSQVo);

        // 将此部分数据作为内容 用于生成二维码图片
        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        // 将二维码上传到阿里云
        String url = aliOSSUtils.upload(image);

        // 将url保存到学生的二维码云端路径字段当中
        platePass.setQrCode(url);
        // 更新学生信息
        this.updateById(platePass);
        // 返回url路径
        return url;
    }

    /**
     * 学生查询自身通行证信息
     */
    @Override
    public List<StudentPlatePassVo> getStudentPass(Students student) {
        //通过当前学生的 学号 和 学校名称 去 PlatePass表中查询信息

        String studentNumber = student.getStudentNumber();
        String schoolName = student.getSchoolName();
        //创建条件构造器
        LambdaQueryWrapper<PlatePass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //添加查询条件
        lambdaQueryWrapper.eq(PlatePass::getStudentNumber, studentNumber)
                .eq(PlatePass::getSchoolName, schoolName);

        List<PlatePass> list = this.list(lambdaQueryWrapper);
        //将查询到的数据拷贝到StudentPlatePassVo中

        List<StudentPlatePassVo> studentPlatePassVos = new ArrayList<>();

        for (PlatePass platePass : list) {
            StudentPlatePassVo studentPlatePassVo = new StudentPlatePassVo();
            BeanUtils.copyProperties(platePass, studentPlatePassVo);
            studentPlatePassVos.add(studentPlatePassVo);
        }

        return studentPlatePassVos;

    }
}




