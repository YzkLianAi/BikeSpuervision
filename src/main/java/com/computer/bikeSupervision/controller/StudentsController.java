package com.computer.bikeSupervision.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Api(tags = "学生信息管理")
@CrossOrigin
@RequestMapping("/Students")
public class StudentsController {
    @Autowired
    StudentsService studentsService;

    @ApiOperation(value = "学生登录接口", notes = "需要转递学号studentNumber和密码password")
    @PostMapping("/login")
    public Result<String> login(@RequestBody StudentLoginDto studentLoginDto) {
        //获取当前学生的学号
        String studentId = studentLoginDto.getStudentNumber();
        //获取经过md5加密后的密码
        String md5Password = DigestUtils.md5DigestAsHex(studentLoginDto.getPassword().getBytes());

        log.info("登录的学生学号:{},密码:{}", studentId, md5Password);

        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Students::getStudentNumber, studentId)
                .eq(Students::getPassword, md5Password);

        Students student = studentsService.getOne(queryWrapper);
        if (student != null) {
            Map<String, Object> claims = new HashMap<>();

            claims.put("id", student.getId());
            claims.put("name", student.getStudentName());
            claims.put("number", student.getStudentNumber());

            String jwt = JwtUtils.generateJwt(claims);
            String token = "Bearea" + " " + jwt;
            log.info(token);
            //将生成的令牌返回 后续前端的每次请求都必须携带这个令牌
            return Result.setToken(token);
        }
        throw new CustomException("用户名或密码错误");

    }


    @ApiOperation(value = "获取当前登录的学生信息")
    @GetMapping("/getStudentById")
    public Result<Students> getStudentById() {
        LambdaQueryWrapper<Students> queryWrapper = new LambdaQueryWrapper<>();

        //根据当前线程获取id
        Long currentId = BaseContext.getCurrentId();

        //根据id查询学生信息
        queryWrapper.eq(Students::getId, currentId);
        //获取匹配的学生信息
        Students student = studentsService.getOne(queryWrapper);

        return Result.success(student);
    }


}
