package com.computer.bikeSupervision.controller.webController;


import cn.hutool.core.bean.BeanUtil;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.dto.StudentRegisterDto;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.service.StudentsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "学生信息管理")
@CrossOrigin
@RequestMapping("/Web/Students")
public class StudentsController {
    @Autowired
    StudentsService studentsService;

    @ApiOperation(value = "学生登录接口", notes = "需要转递学号studentNumber和密码password")
    @PostMapping("/login")
    public Result<String> login(@ApiParam("学生登录Dto") @RequestBody StudentLoginDto studentLoginDto) {
        //获取当前学生的学号
        String studentId = studentLoginDto.getStudentNumber();
        //获取经过md5加密后的密码
        String md5Password = DigestUtils.md5DigestAsHex(studentLoginDto.getPassword().getBytes());
        studentLoginDto.setPassword(md5Password);
        log.info("登录的学生学号:{},密码:{},学校:{}", studentId, md5Password, studentLoginDto.getSchoolName());

        String token = studentsService.login(studentLoginDto);

        return Result.setToken(token);

    }

    @ApiOperation(value = "学生注册")
    @PostMapping("/register")
    public Result<String> register(@ApiParam("学生注册信息") @RequestBody StudentRegisterDto student) {
        log.info("学生注册信息:{}", student);

        //将Dto 拷贝到 实体类
        Students newStudent = BeanUtil.copyProperties(student, Students.class);
        //执行新增操作
        studentsService.save(newStudent);
        //配置注册信息
        studentsService.register(newStudent);

        //返回注册成功
        return Result.success("注册成功");
    }

    @ApiOperation(value = "获取当前登录的学生信息")
    @GetMapping("/getStudentById")
    public Result<Students> getStudentById() {
        //根据当前线程获取id
        Long currentId = BaseContext.getCurrentId();
        //获取匹配的学生信息
        Students student = studentsService.getById(currentId);

        return Result.success(student);
    }

    @ApiOperation(value = "学生信息修改")
    @PostMapping("/update")
    public Result<String> updateStudent(@ApiParam("学生信息") @RequestBody Students students) {
        log.info("学生信息修改:{}", students);
        //修改学生信息
        studentsService.update(students);

        return Result.success("修改成功");
    }


    @ApiOperation(value = "学生信息分页查询")
    @GetMapping("/StudentsPage")
    public Result<PageBean> studentPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        String name) {
        Long currentId = BaseContext.getCurrentId();
        log.info("当前操作人id:{}", currentId);


        log.info("page = {} , pageSize = {}, name = {}", page, pageSize, name);

        PageBean pageInfo = studentsService.getStudentsPage(page, pageSize, name, currentId);

        return Result.success(pageInfo);
    }

}
