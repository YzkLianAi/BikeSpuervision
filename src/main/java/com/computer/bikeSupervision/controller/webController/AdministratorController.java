package com.computer.bikeSupervision.controller.webController;

import cn.hutool.core.bean.BeanUtil;
import com.computer.bikeSupervision.common.BaseContext;
import com.computer.bikeSupervision.common.Result;
import com.computer.bikeSupervision.pojo.dto.AdministratorLoginDto;
import com.computer.bikeSupervision.pojo.dto.AdministratorRegisterDto;
import com.computer.bikeSupervision.pojo.entity.Administrator;
import com.computer.bikeSupervision.service.AdministratorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Api(tags = "管理员信息管理")
@CrossOrigin
@RequestMapping("/Web/Administrator")
public class AdministratorController {
    @Autowired
    AdministratorService administratorService;


    @ApiOperation(value = "管理员登录接口")
    @PostMapping("/login")
    public Result<String> login(@ApiParam("管理员登录Dto") @RequestBody AdministratorLoginDto administratorLoginDto) {
        //获取当前管理员账号
        String adminNumber = administratorLoginDto.getAdminNumber();
        //获取经过md5加密后的密码
        String md5Password = DigestUtils.md5DigestAsHex(administratorLoginDto.getPassword().getBytes());
        // 将密码加密后存入dto
        administratorLoginDto.setPassword(md5Password);

        log.info("登录的管理员账号:{},密码:{},学校:{}", adminNumber, md5Password, administratorLoginDto.getSchoolName());

        String token = administratorService.login(administratorLoginDto);

        return Result.setToken(token);
    }

    @ApiOperation(value = "管理员注册")
    @PostMapping("/register")
    public Result<String> register(@ApiParam("管理员注册信息") @RequestBody AdministratorRegisterDto administratorRegisterDto) {
        log.info("学生注册信息:{}", administratorRegisterDto);

        //将Dto 拷贝到 实体类
        Administrator administrator = BeanUtil.copyProperties(administratorRegisterDto, Administrator.class);
        //执行新增操作
        administratorService.save(administrator);
        //配置注册信息
        administratorService.register(administrator);

        //返回注册成功
        return Result.success("注册成功");
    }

    @ApiOperation(value = "获取当前登录的管理员信息")
    @GetMapping("/getAdministratorById")
    public Result<Administrator> getStudentById() {
        //根据当前线程获取id
        String currentId = BaseContext.getCurrentId();
        log.info("获取当前登录信息的id:{}", currentId);
        //获取匹配的管理员信息
        Administrator administrator = administratorService.getById(currentId);

        return Result.success(administrator);
    }


    @ApiOperation(value = "管理员信息修改")
    @PostMapping("/update")
    public Result<String> updateStudent(@RequestBody Administrator administrator) {
        log.info("管理员信息修改:{}", administrator);
        //修改学生信息
        administratorService.update(administrator);

        return Result.success("修改成功");
    }


}
