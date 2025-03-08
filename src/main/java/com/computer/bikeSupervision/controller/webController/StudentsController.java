package com.computer.bikeSupervision.controller.webController;


import cn.hutool.core.bean.BeanUtil;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.CustomException;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.StudentLoginDto;
import com.computer.bikeSupervision.pojo.dto.StudentRegisterDto;
import com.computer.bikeSupervision.pojo.entity.PageBean;
import com.computer.bikeSupervision.pojo.entity.Students;
import com.computer.bikeSupervision.pojo.entity.Violation;
import com.computer.bikeSupervision.pojo.vo.StudentPlatePassVo;
import com.computer.bikeSupervision.service.PlatePassService;
import com.computer.bikeSupervision.service.StudentsService;
import com.computer.bikeSupervision.service.ViolationService;
import com.computer.bikeSupervision.utils.CustomSaltPasswordEncoder;
import com.computer.bikeSupervision.utils.VerificationCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(tags = "学生信息管理")
@CrossOrigin
@RequestMapping("/Web/Students")
public class StudentsController {
    @Autowired
    StudentsService studentsService;

    @Autowired
    PlatePassService platePassService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ViolationService violationService;

    @Autowired
    private VerificationCodeUtil verificationCodeUtil;

    @ApiOperation(value = "学生登录接口", notes = "需要转递邮箱和密码password")
    @PostMapping("/login")
    public Result<String> login(@ApiParam("学生登录Dto") @RequestBody StudentLoginDto studentLoginDto) {
        //获取当前学生的邮箱
        //String email = studentLoginDto.getEmail();
        //获取经过md5加密后的密码
        //String md5Password = DigestUtils.md5DigestAsHex(studentLoginDto.getPassword().getBytes());
        //studentLoginDto.setPassword(md5Password);

        String token = studentsService.login(studentLoginDto);

        return Result.ok(token);

    }

    @ApiOperation(value = "学生注册")
    @PostMapping("/register")
    public Result<String> register(@ApiParam("学生注册信息") @RequestBody StudentRegisterDto student) {
        log.info("学生注册信息:{}", student);
        // 两个密码重复校验
        if (!student.getPassword().equals(student.getRePassword())) {
            // 密码不一致，返回错误信息
            throw new CustomException("两次输入的密码不一致");
        }

        // 验证码校验
        if (!verificationCodeUtil.verifyCode(student.getEmail(), student.getCode())) {
            // 验证码校验失败，返回错误信息
            throw new CustomException("验证码错误,请重新输入");
        }

        //做邮箱唯一性校验
        if (studentsService.query().eq("email", student.getEmail()).one() != null) {
            throw new CustomException("该邮箱已被注册");
        }
        String encodedPassword = CustomSaltPasswordEncoder.encodePassword(student.getPassword());
        // 将 DTO 拷贝到实体类
        Students newStudent = BeanUtil.copyProperties(student, Students.class);
        newStudent.setPassword(encodedPassword);
        // 执行新增操作
        studentsService.save(newStudent);
        // 配置注册信息
        studentsService.register(newStudent);

        // 返回注册成功
        return Result.ok("注册成功");
    }

    @ApiOperation("登出接口")
    @GetMapping("/logout")
    public Result<String> logout() {
        String currentId = BaseContext.getCurrentId();
        log.info("当前登录的学生id:{}", currentId);
        Students students = studentsService.getById(currentId);
        log.info("{} 正在尝试登出...", students.getEmail());
        Boolean delete = redisTemplate.delete("student:" + students.getId());
        if (Boolean.FALSE.equals(delete)) {
            return Result.fail("退出失败，不要乱搞");
        }
        log.info("{} 成功登出...", students.getEmail());
        return Result.ok("退出登录成功");
    }

    @ApiOperation(value = "获取当前登录的学生信息")
    @GetMapping("/getStudentById")
    public Result<Students> getStudentById() {
        // 根据当前线程获取 id
        String currentId = BaseContext.getCurrentId();


        Students student = studentsService.getById(currentId);

        return Result.ok(student);
    }

    @ApiOperation(value = "学生信息修改")
    @PostMapping("/update")
    public Result<String> updateStudent(@ApiParam("学生信息") @RequestBody Students students) {
        log.info("学生信息修改:{}", students);
        //修改学生信息
        studentsService.update(students);

        return Result.ok("修改成功");
    }


    @ApiOperation(value = "学生信息分页查询")
    @GetMapping("/StudentsPage")
    public Result<PageBean> studentPage(@RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "10") int pageSize,
                                        String name) {
        String currentId = BaseContext.getCurrentId();
        log.info("当前操作人id:{}", currentId);

        log.info("page = {} , pageSize = {}, name = {}", page, pageSize, name);

        PageBean pageInfo = studentsService.getStudentsPage(page, pageSize, name, currentId);

        return Result.ok(pageInfo);
    }

    @ApiOperation(value = "查询自己的违章记录")
    @GetMapping("/getMyViolationRecords")
    public Result<List<Violation>> getMyViolationRecords() {
        // 根据当前线程获取 id
        String currentId = BaseContext.getCurrentId();

        // 获取学生学号
        String studentNumber = studentsService.getById(currentId).getStudentNumber();
        log.info("学生学号:{}", studentNumber);
        // 通过学号查询学生拥有的车牌号列表
        List<String> licensePlates = platePassService.getLicensePlatesByStudentNumber(studentNumber);
        // 根据车牌号列表查询违章记录
        List<Violation> violationRecords = violationService.getViolationsByLicensePlates(licensePlates);
        return Result.ok(violationRecords);
    }

    @ApiOperation(value = "学生查询自己所拥有的通行证信息")
    @GetMapping("/getStudentPass")
    public Result<List<StudentPlatePassVo>> getStudentPass() {
        //根据当前线程获取id
        String currentId = BaseContext.getCurrentId();
        //获取匹配的学生信息
        Students student = studentsService.getById(currentId);

        List<StudentPlatePassVo> platePassList = platePassService.getStudentPass(student);
        //查询出的通行证信息
        log.info("查询出的通行证信息:{}", platePassList);

        return Result.ok(platePassList);
    }

}
