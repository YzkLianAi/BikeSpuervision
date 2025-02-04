package com.computer.bikeSupervision.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.mapper.PlatePassMapper;
import com.computer.bikeSupervision.mapper.StudentsMapper;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.PlatePass;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.vo.StudentsPageVo;
import com.computer.bikeSupervision.service.AdministratorService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private PlatePassMapper platePassMapper;

    /**
     * 学生二维码生成
     *
     * @param id
     * @return
     * @throws Exception
     */
    //TODO 后续要删除
    /*@Override
    public String generateSqCode(Long id) throws Exception {
        // 根据id查询当前人的信息
        Students student = this.getById(id);
        // 断是否绑定车牌号和通行证号
        if (student.getPlateNumber() == null){
            throw new CustomException("请先绑定车牌号");
        }

        if (student.getPassNumber() == null){
            throw new CustomException("请先绑定通行证号");
        }

        StudentSQVo studentSQVo = new StudentSQVo();
        // 第一个参数是原始数据 第二参数 为 拷贝的对象目标
        BeanUtils.copyProperties(student, studentSQVo);

        log.info("拷贝好的属性：{}", studentSQVo);
        // 将实体类转换成json格式的数据 用于生成二维码
        String json = JSONObject.toJSONString(studentSQVo);

        // 将此部分数据作为内容 用于生成二维码图片
        MultipartFile image = qrCodeGenerator.generateQRCodeAsMultipartFile(json);
        // 将二维码上传到七牛云
        String url = qiniuCloudUtils.uploadImage(image);

        // 将url保存到学生的二维码云端路径字段当中
        student.setQrCode(url);
        // 更新学生信息
        this.updateById(student);
        // 返回url路径
        return url;
    }*/

    /**
     * 学生登录
     */
    @Override
    public String login(StudentLoginDto studentLoginDto) {

        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Students::getStudentNumber, studentLoginDto.getStudentNumber())
                .eq(Students::getPassword, studentLoginDto.getPassword())
                .eq(Students::getSchoolName, studentLoginDto.getSchoolName());

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
    public PageBean getStudentsPage(int page, int pageSize, String name, Long currentId) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);//设置分页参数

        Administrator administrator = administratorService.getById(currentId);
        String schoolName = administrator.getSchoolName();

        //构造条件构造器
        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Students::getStudentName, name)
                .eq(Students::getSchoolName, schoolName);
        //添加排序条件
        //根据最后的更新时间进行降序排序 Desc：降序 Asc：升序
        queryWrapper.orderByDesc(Students::getUpdateTime);

        Page<Students> studentPage = PageHelper.startPage(page, pageSize)
                .doSelectPage(() -> this.list(queryWrapper));

        // 将 Students 列表转换为 StudentsPageVo 列表，并查询是否拥有电瓶车和通行证
        List<StudentsPageVo> studentsPageVoList = new ArrayList<>();

        for (Students student : studentPage.getResult()) {
            StudentsPageVo studentsPageVo = new StudentsPageVo();
            //先拷贝学生的基础信息数据
            BeanUtils.copyProperties(student, studentsPageVo);

            LambdaQueryWrapper<PlatePass> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(PlatePass::getStudentNumber, student.getStudentName());
            // 查询该学生的通行审核记录
            List<PlatePass> platePasses = platePassMapper.selectList(lambdaQueryWrapper);

            // 判断是否拥有电瓶车和通行证
            studentsPageVo.setHasBike(!platePasses.isEmpty() && StringUtils.isNotEmpty(platePasses.get(0).getPlateNumber()));
            studentsPageVo.setHasPass(!platePasses.isEmpty() && StringUtils.isNotEmpty(platePasses.get(0).getPassNumber()));

            studentsPageVoList.add(studentsPageVo);
        }

        // 手动构建 Page<StudentsPageVo> 对象
        Page<StudentsPageVo> studentsPageVoPage = new Page<>();
        studentsPageVoPage.setPageNum(studentPage.getPageNum());
        studentsPageVoPage.setPageSize(studentPage.getPageSize());
        studentsPageVoPage.setTotal(studentPage.getTotal());
        studentsPageVoPage.addAll(studentsPageVoList);

        return new PageBean(studentsPageVoPage.getTotal(), studentsPageVoPage.getResult());

    }

    /**
     * 注册信息配置
     *
     * @param newStudent
     */
    @Override
    public void register(Students newStudent) {
        //需要重新设置一下createUser 和 updateUser
        //设置当前线程id 用于后续公共字段填充
        BaseContext.setCurrentId(newStudent.getId());
        //自己是自己的创建人
        newStudent.setCreateUser(newStudent.getId());
        log.info("新注册的学生信息:{}", newStudent);
        //创建时间在新增的时候就已经提供了 此处只会修改 修改时间 和 创建人
        this.updateById(newStudent);
    }

    /**
     * 根据id修改学生信息
     *
     * @param students
     */
    @Override
    public void update(Students students) {
        // 需要先比对密码是否经过修改 -> 如果密码是将 123456 重新加密后的结果 则说明密码没有经过修改不需要再经过md5加密
        // 将其重新设置成 123456 经过md5加密后的结果
        if (students.getPassword().equals(DigestUtils.md5DigestAsHex("123456".getBytes()))) {
            students.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        } else {
            students.setPassword(DigestUtils.md5DigestAsHex(students.getPassword().getBytes()));
        }

        this.updateById(students);
    }
}




