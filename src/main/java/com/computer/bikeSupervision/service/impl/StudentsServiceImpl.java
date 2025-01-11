package com.computer.bikeSupervision.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentSQVo;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.JwtUtils;
import com.computer.bikeSupervision.utils.QRCodeGenerator;
import com.computer.bikeSupervision.utils.QiniuCloudUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    @Autowired
    private QiniuCloudUtils qiniuCloudUtils;

    /**
     * 学生二维码生成
     *
     * @param id
     * @return
     * @throws Exception
     */
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

    /**
     * 学生登录
     *
     * @param studentId
     * @param md5Password
     * @return
     */
    @Override
    public String login(String studentId, String md5Password) {

        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Students::getStudentNumber, studentId)
                .eq(Students::getPassword, md5Password);

        Students student = this.getOne(queryWrapper);
        if (student != null) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("id", student.getId());
            claims.put("name", student.getStudentName());
            claims.put("number", student.getStudentNumber());

            String jwt = JwtUtils.generateJwt(claims);
            String token = "Bearea" + " " + jwt;
            log.info(token);
            //将生成的令牌返回 后续前端的每次请求都必须携带这个令牌
            return token;
        }
        throw new CustomException("用户名或密码错误");

    }

    /**
     * 分页查询学生信息
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public Page<Students> getStudentsPage(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Students> pageInfo = new Page<>(page, pageSize);

        //构造条件构造器
        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Students::getStudentName, name);
        //添加排序条件
        //根据最后的更新时间进行降序排序 Desc：降序 Asc：升序
        queryWrapper.orderByDesc(Students::getUpdateTime);

        //执行查询
        this.page(pageInfo, queryWrapper);
        return pageInfo;
    }

    @Override
    public void register(Students newStudent) {
        //需要重新设置一下createUser 和 updateUser
        BaseContext.setCurrentId(newStudent.getId());
        newStudent.setCreateUser(newStudent.getId());
        log.info("新注册的学生信息:{}", newStudent);
        this.updateById(newStudent);
    }
}




