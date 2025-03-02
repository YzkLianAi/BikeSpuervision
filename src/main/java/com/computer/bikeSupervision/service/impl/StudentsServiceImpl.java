package com.computer.bikeSupervision.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import com.computer.bikeSupervision.utils.CustomSaltPasswordEncoder;
import com.computer.bikeSupervision.utils.JwtUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class StudentsServiceImpl extends ServiceImpl<StudentsMapper, Students> implements StudentsService {

    @Autowired
    private AdministratorService administratorService;

    @Autowired
    private PlatePassMapper platePassMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 学生登录
     */
    @Override
    public String login(StudentLoginDto studentLoginDto) {
        log.info("当前登录学生：{}", studentLoginDto);

        //先比对邮箱是否错误
        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Students::getEmail, studentLoginDto.getEmail());

        Students student = this.getOne(queryWrapper);
        if (student == null) {
            throw new CustomException("邮箱错误,请重新输入");
        }

        //比对原始密码 和 加密密码
        // 验证密码 前者为传递的原密码 后者为 数据库中存放的加密密码
        boolean isMatch = CustomSaltPasswordEncoder.matches(studentLoginDto.getPassword(), student.getPassword());

        if (isMatch) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("id", student.getId());
            claims.put("name", student.getStudentName());
            claims.put("number", student.getStudentNumber());

            String jwt = JwtUtils.generateJwt(claims);
            String token = "Bearea" + " " + jwt;
            log.info(token);
            //将生成的令牌返回 后续前端的每次请求都必须携带这个令牌

            // 将学生信息缓存到 Redis 中，设置过期时间为 1 小时
            // 将学生对象存入Redis
            String key = "student:" + student.getId();
            String json = JSONObject.toJSONString(student);
            redisTemplate.opsForValue().set(key, json, 12, TimeUnit.HOURS);
            return token;
        }
        throw new CustomException("密码错误,请重新输入");
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
    public PageBean getStudentsPage(int page, int pageSize, String name, String currentId) {
        //1.设置分页参数
        PageHelper.startPage(page, pageSize);//设置分页参数

        Administrator administrator = administratorService.getById(currentId);
        if (administrator == null) {
            throw new CustomException("非管理员账户");
        }

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
     */
    @Override
    public void update(Students students) {
        // 获取学生原信息
        Students originalStudent = this.getById(students.getId());
        // 判断密码是否被修改
        if (originalStudent != null && students.getPassword() != null) {
            // 如果新密码和原加密密码相同，说明学生没有修改密码，直接使用原加密密码
            if (students.getPassword().equals(originalStudent.getPassword())) {
                students.setPassword(originalStudent.getPassword());
            } else {
                // 新密码有修改，进行加密
                students.setPassword(CustomSaltPasswordEncoder.encodePassword(students.getPassword()));
            }
        }
        this.updateById(students);
    }

    @Override
    public String getStudentNameByStudentNumber(String studentNumber) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getStudentNumber, studentNumber);
        Students student = this.getOne(wrapper);
        return student != null ? student.getStudentName() : null;
    }

    //扣分
    @Override
    public void updateStudentScore(String studentNumber, BigDecimal deductionScore) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getStudentNumber, studentNumber);
        Students student = this.getOne(wrapper);
        //加扣分
        student.setScore(student.getScore().add(deductionScore));
        this.updateById(student);
    }

    //查询学生的学院
    @Override
    public String getCollegeByStudentNumber(String studentNumber) {
        LambdaQueryWrapper<Students> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Students::getStudentNumber, studentNumber);
        Students student = this.getOne(wrapper);
        return student != null ? student.getCollege() : null;
    }
}




