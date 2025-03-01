package com.computer.bikeSupervision.controller.webController;


import cn.hutool.core.bean.BeanUtil;
import com.computer.bikeSupervision.common.BaseContext;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    @ApiOperation(value = "学生登录接口", notes = "需要转递邮箱和密码password")
    @PostMapping("/login")
    public Result<String> login(@ApiParam("学生登录Dto") @RequestBody StudentLoginDto studentLoginDto) {
        //获取当前学生的邮箱
        String studentId = studentLoginDto.getEmail();
        //获取经过md5加密后的密码
        String md5Password = DigestUtils.md5DigestAsHex(studentLoginDto.getPassword().getBytes());
        studentLoginDto.setPassword(md5Password);
        log.info("登录的学生邮箱:{},密码:{}", studentId, md5Password);

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
        // 根据当前线程获取 id
        Long currentId = BaseContext.getCurrentId();

        // 先从 Redis 中获取学生信息
        Students student = (Students) redisTemplate.opsForValue().get("student:" + currentId);

        if (student == null) {
            // 如果 Redis 中不存在，再从数据库中查询
            student = studentsService.getById(currentId);
            if (student != null) {
                // 将查询到的学生信息缓存到 Redis 中，设置过期时间为 43200000L 毫秒（12 小时）
                redisTemplate.opsForValue().set("student:" + student.getId(), student, 12, TimeUnit.HOURS);
            }
        }

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

    @ApiOperation(value = "查询自己的违章记录")
    @GetMapping("/getMyViolationRecords")
    public Result<List<Violation>> getMyViolationRecords() {
        // 根据当前线程获取 id
        Long currentId = BaseContext.getCurrentId();

        // 获取学生学号
        String studentNumber = studentsService.getById(currentId).getStudentNumber();

        // 通过学号查询学生拥有的车牌号列表
        List<String> licensePlates = platePassService.getLicensePlatesByStudentNumber(studentNumber);
        // 根据车牌号列表查询违章记录
        List<Violation> violationRecords = violationService.getViolationsByLicensePlates(licensePlates);
        return Result.success(violationRecords);
    }

    //TODO 学生查询自己所拥有的通行证信息
    @ApiOperation(value = "学生查询自己所拥有的通行证信息")
    @GetMapping("/getStudentPass")
    public Result<List<StudentPlatePassVo>> getStudentPass() {
        //根据当前线程获取id
        Long currentId = BaseContext.getCurrentId();
        //获取匹配的学生信息
        Students student = studentsService.getById(currentId);

        List<StudentPlatePassVo> platePassList = platePassService.getStudentPass(student);

        return Result.success(platePassList);
    }

}
